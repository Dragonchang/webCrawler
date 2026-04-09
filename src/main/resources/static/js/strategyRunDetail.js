$(function() {
    var runId = $('#run_id').val();
    var ws = null;

    loadSummary();
    loadLogs();
    loadResults();
    loadSnapshot();
    connectWebSocket();

    function loadSummary() {
        $.get(base_url + '/strategyRun/detail', {runId: runId}, function(resp) {
            if (resp.code == 0 && resp.data) {
                renderSummary(resp.data);
                updateLiveStatus(resp.data.runStatus || 'UNKNOWN', percentByStatus(resp.data.runStatus), resp.data.errorMessage || '', null, null, null);
            }
        });
    }

    function loadLogs() {
        $.get(base_url + '/strategyRun/logList', {runId: runId}, function(resp) {
            if (resp.code == 0 && resp.data) {
                var text = '';
                for (var i = 0; i < resp.data.length; i++) {
                    var item = resp.data[i];
                    text += '[' + item.logLevel + '] ' + item.logTime + ' ' + item.content + '\n';
                }
                $('#log_panel').text(text);
            }
        });
    }

    function loadResults() {
        $.get(base_url + '/strategyRun/resultList', {runId: runId}, function(resp) {
            if (resp.code == 0 && resp.data) {
                renderResults(resp.data);
            }
        });
    }

    function loadSnapshot() {
        $.get(base_url + '/strategyRun/snapshot', {runId: runId}, function(resp) {
            if (resp.code == 0 && resp.data) {
                handlePushMessage(resp.data);
            }
        });
    }

    function connectWebSocket() {
        var protocol = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
        var wsUrl = protocol + window.location.host + base_url + '/ws/strategyRun';
        ws = new WebSocket(wsUrl);
        ws.onopen = function() {
            updateLiveStatus('CONNECTED', null, 'WebSocket已连接，等待运行消息', null, null, null);
            ws.send(JSON.stringify({runId: parseInt(runId)}));
        };
        ws.onmessage = function(event) {
            var data = JSON.parse(event.data || '{}');
            if (!data.runId || String(data.runId) !== String(runId)) {
                return;
            }
            handlePushMessage(data);
        };
        ws.onclose = function() {
            updateLiveStatus($('#live_status').text(), null, 'WebSocket已断开，3秒后重连', null, null, null);
            setTimeout(connectWebSocket, 3000);
        };
        ws.onerror = function() {
            updateLiveStatus($('#live_status').text(), null, 'WebSocket连接异常', null, null, null);
        };
    }

    function handlePushMessage(data) {
        if (data.eventType === 'LOG') {
            appendLog(data);
            updateLiveStatus($('#live_status').text(), null, data.message || '', data.stageCode, data.current, data.total);
            return;
        }
        updateLiveStatus(data.status || $('#live_status').text(), data.progress, data.message || '', data.stageCode, data.current, data.total);
        if (data.eventType === 'DONE') {
            loadSummary();
            loadLogs();
            loadResults();
        }
    }

    function renderSummary(d) {
        var html = '';
        html += buildCell('运行ID', d.id);
        html += buildCell('策略名称', d.strategyName);
        html += buildCell('策略编码', d.strategyCode);
        html += buildCell('版本号', d.versionNo);
        html += buildCell('执行器', d.engineType);
        html += buildCell('脚本类型', d.scriptType);
        html += buildCell('运行状态', d.runStatus);
        html += buildCell('运行方式', d.runType);
        html += buildCell('开始时间', d.startTime);
        html += buildCell('结束时间', d.endTime);
        html += buildCell('耗时(ms)', d.durationMs);
        html += buildCell('结果数量', d.resultCount);
        html += buildCell('错误摘要', d.errorMessage || '');
        $('#summary_wrapper').html(html);
    }

    function renderResults(data) {
        $('#result_panel').text(JSON.stringify(data, null, 2));
        var html = '';
        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            html += '<tr>' +
                '<td>' + safe(item.rankNo) + '</td>' +
                '<td>' + safe(item.stockCode) + '</td>' +
                '<td>' + safe(item.stockName) + '</td>' +
                '<td>' + safe(item.actionType) + '</td>' +
                '<td>' + safe(item.score) + '</td>' +
                '<td>' + safe(item.reason) + '</td>' +
                '<td><pre style="white-space:pre-wrap;">' + safe(item.factorDetail) + '</pre></td>' +
                '</tr>';
        }
        $('#result_tbody').html(html);
    }

    function appendLog(item) {
        var line = '[' + safe(item.logLevel) + '] ' + safe(item.logTime) + ' ' + safe(item.message) + '\n';
        $('#log_panel').text($('#log_panel').text() + line);
    }

    function updateLiveStatus(status, progress, message, stageCode, current, total) {
        if (status != null) {
            $('#live_status').text(status);
        }
        if (progress != null && progress !== '') {
            $('#live_progress_bar').css('width', progress + '%');
            $('#live_progress_text').text(progress + '%');
        }
        if (message != null) {
            var detail = message;
            if (stageCode) {
                detail += ' [' + stageCode + ']';
            }
            if (current != null && total != null) {
                detail += ' ' + current + '/' + total;
            }
            $('#live_message').text(detail);
        }
    }

    function percentByStatus(status) {
        if (status === 'SUCCESS' || status === 'FAIL') {
            return 100;
        }
        if (status === 'RUNNING') {
            return 10;
        }
        return 0;
    }

    function buildCell(label, value) {
        return '<div class="col-sm-3"><div class="description-block" style="border-right:1px solid #f4f4f4; margin-bottom:10px;">' +
            '<h5 class="description-header">' + safe(value) + '</h5>' +
            '<span class="description-text">' + label + '</span>' +
            '</div></div>';
    }

    function safe(value) {
        return value == null ? '' : value;
    }
});
