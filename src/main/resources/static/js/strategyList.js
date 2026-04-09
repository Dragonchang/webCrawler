$(function() {
    var tableData = {};
    var liveStatusMap = {};
    var ws = null;

    connectWebSocket();

    var strategyTable = $("#strategy_list").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/strategy/pageList",
            type: "post",
            data: function(d) {
                return {
                    strategyName: $('#strategy_name').val(),
                    strategyCode: $('#strategy_code').val(),
                    category: $('#category').val(),
                    status: $('#status').val() == '-1' ? '' : $('#status').val(),
                    start: d.start,
                    length: d.length
                };
            }
        },
        "searching": false,
        "ordering": false,
        "columns": [
            {"data": 'id', "visible": false},
            {"data": 'strategyName'},
            {"data": 'strategyCode'},
            {"data": 'category'},
            {"data": 'scriptType'},
            {"data": 'validateStatus'},
            {"data": 'publishedVersionNo'},
            {"data": 'status', "render": function(data){ return data == 1 ? '启用' : (data == 2 ? '停用' : '草稿'); }},
            {"data": 'scheduleType'},
            {"data": 'lastRunStatus'},
            {"data": null, "render": function(data, type, row){
                tableData['key' + row.id] = row;
                return renderLiveStatusCell(row.id);
            }},
            {"data": 'lastRunTime'},
            {"data": 'updatedTime'},
            {"data": 'detail', "render": function(data, type, row){
                tableData['key' + row.id] = row;
                return '<p id="' + row.id + '">' +
                    '<button class="btn btn-warning btn-xs edit" type="button">编辑</button> ' +
                    '<button class="btn btn-info btn-xs publish" type="button">发布</button> ' +
                    '<button class="btn btn-success btn-xs run" type="button">运行</button> ' +
                    '<button class="btn btn-primary btn-xs logs" type="button">记录</button> ' +
                    '<button class="btn btn-default btn-xs toggle" type="button">' + (row.status == 1 ? '停用' : '启用') + '</button>' +
                    '</p>';
            }}
        ],
        "language": {
            "sProcessing": "处理中...",
            "sLengthMenu": "每页 _MENU_ 条记录",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
            "sInfoEmpty": "无记录",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sSearch": "搜索",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "oPaginate": {"sFirst": "首页", "sPrevious": "上页", "sNext": "下页", "sLast": "末页"}
        },
        "drawCallback": function() {
            refreshLiveCells();
            refreshDashboard();
        }
    });

    $('#searchBtn').on('click', function(){
        strategyTable.fnDraw();
    });

    $('#createBtn').on('click', function(){
        window.open(base_url + '/strategy/edit', '_self');
    });

    $('#strategy_list').on('click', '.edit', function(){
        var id = $(this).parent('p').attr('id');
        window.open(base_url + '/strategy/edit?id=' + id, '_self');
    });

    $('#strategy_list').on('click', '.publish', function(){
        var id = $(this).parent('p').attr('id');
        $.post(base_url + '/strategy/publish?id=' + id, function(resp){
            if (resp.code == 0) {
                alert('发布成功');
                strategyTable.fnDraw();
            } else {
                alert(resp.message || '发布失败');
            }
        });
    });

    $('#strategy_list').on('click', '.run', function(){
        var id = $(this).parent('p').attr('id');
        var row = tableData['key' + id];
        var params = row.defaultParams || row.default_params || '{"minMarketCap":100,"maxPe":30,"minPrice":5,"limit":20}';
        markRunning(parseInt(id), 'RUNNING', 1, '提交运行请求中', 'RUN_CREATED', null, null, null);
        $.ajax({
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: base_url + '/strategyRun/execute',
            data: JSON.stringify({strategyId: parseInt(id), params: params}),
            success: function(resp){
                if (resp.code == 0) {
                    var runId = resp.data.runId;
                    markRunning(parseInt(id), 'RUNNING', 3, '运行已创建，runId=' + runId, 'RUN_CREATED', runId, null, null);
                    updateDashboard('RUNNING', 3, '策略 #' + id + ' 已创建运行，runId=' + runId);
                    window.open(base_url + '/strategyRun/detailPage?runId=' + runId, '_self');
                } else {
                    markRunning(parseInt(id), 'FAIL', 100, resp.message || '运行失败', 'FAILED', null, null, null);
                    alert(resp.message || '运行失败');
                }
            },
            error: function() {
                markRunning(parseInt(id), 'FAIL', 100, '运行请求失败', 'FAILED', null, null, null);
                alert('运行请求失败');
            }
        });
    });

    $('#strategy_list').on('click', '.logs', function(){
        window.open(base_url + '/strategyRun', '_self');
    });

    $('#strategy_list').on('click', '.toggle', function(){
        var id = $(this).parent('p').attr('id');
        var row = tableData['key' + id];
        var targetStatus = row.status == 1 ? 2 : 1;
        $.post(base_url + '/strategy/changeStatus?id=' + id + '&status=' + targetStatus, function(resp){
            if (resp.code == 0) {
                strategyTable.fnDraw();
            } else {
                alert(resp.message || '状态更新失败');
            }
        });
    });

    function connectWebSocket() {
        var protocol = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
        var wsUrl = protocol + window.location.host + base_url + '/ws/strategyRun';
        ws = new WebSocket(wsUrl);
        ws.onopen = function() {
            updateDashboard('CONNECTED', null, 'WebSocket已连接，等待策略运行消息');
            subscribeCurrentRows();
        };
        ws.onmessage = function(event) {
            var data = JSON.parse(event.data || '{}');
            if (!data.strategyId) {
                return;
            }
            liveStatusMap['key' + data.strategyId] = buildLiveState(data);
            refreshLiveCell(data.strategyId);
            refreshDashboard();
        };
        ws.onclose = function() {
            updateDashboard($('#live_last_event').text(), null, 'WebSocket已断开，3秒后重连');
            setTimeout(connectWebSocket, 3000);
        };
        ws.onerror = function() {
            updateDashboard($('#live_last_event').text(), null, 'WebSocket连接异常');
        };
    }

    function subscribeCurrentRows() {
        if (!ws || ws.readyState !== 1) {
            return;
        }
        for (var key in tableData) {
            if (!tableData.hasOwnProperty(key)) {
                continue;
            }
            ws.send(JSON.stringify({strategyId: tableData[key].id}));
        }
    }

    function renderLiveStatusCell(strategyId) {
        var live = liveStatusMap['key' + strategyId] || defaultLiveState();
        return '<div class="js-live-cell" data-strategy-id="' + strategyId + '">' + buildLiveCellHtml(live) + '</div>';
    }

    function buildLiveCellHtml(live) {
        var statusClass = statusClassName(live.status);
        var currentText = '';
        if (live.current != null && live.total != null) {
            currentText = ' · ' + live.current + '/' + live.total;
        }
        var runText = live.runId ? 'runId=' + live.runId : '暂无运行';
        return '<div><span class="run-status-pill ' + statusClass + '">' + safe(live.statusText) + '</span></div>' +
            '<div class="run-status-sub">' + runText + '</div>' +
            '<div class="progress progress-xs" style="margin:6px 0 4px 0;">' +
            '<div class="progress-bar progress-bar-success" style="width:' + safeNumber(live.progress) + '%"></div>' +
            '</div>' +
            '<div class="run-status-text">' + safe(live.message) + '</div>' +
            '<div class="run-status-sub">' + safe(live.stageCode) + ' · ' + safeNumber(live.progress) + '%' + currentText + '</div>';
    }

    function refreshLiveCells() {
        $('.js-live-cell').each(function() {
            var strategyId = $(this).data('strategy-id');
            $(this).html(buildLiveCellHtml(liveStatusMap['key' + strategyId] || defaultLiveState()));
        });
        subscribeCurrentRows();
    }

    function refreshLiveCell(strategyId) {
        $('.js-live-cell[data-strategy-id="' + strategyId + '"]').html(buildLiveCellHtml(liveStatusMap['key' + strategyId] || defaultLiveState()));
    }

    function markRunning(strategyId, status, progress, message, stageCode, runId, current, total) {
        liveStatusMap['key' + strategyId] = {
            strategyId: strategyId,
            runId: runId,
            status: status,
            statusText: statusText(status),
            progress: progress == null ? 0 : progress,
            message: message || '',
            stageCode: stageCode || '-',
            current: current,
            total: total,
            updateTime: new Date().getTime()
        };
        refreshLiveCell(strategyId);
        refreshDashboard();
    }

    function buildLiveState(data) {
        return {
            strategyId: data.strategyId,
            runId: data.runId,
            status: data.status || 'RUNNING',
            statusText: statusText(data.status),
            progress: data.progress == null ? 0 : data.progress,
            message: buildMessage(data),
            stageCode: data.stageCode || data.eventType || '-',
            current: data.current,
            total: data.total,
            updateTime: new Date().getTime()
        };
    }

    function buildMessage(data) {
        if (data.eventType === 'LOG') {
            return '[' + safe(data.logLevel) + '] ' + safe(data.message);
        }
        return data.message || '收到实时更新';
    }

    function refreshDashboard() {
        var runningCount = 0;
        var latest = null;
        for (var key in liveStatusMap) {
            if (!liveStatusMap.hasOwnProperty(key)) {
                continue;
            }
            var item = liveStatusMap[key];
            if (item.status === 'RUNNING') {
                runningCount++;
            }
            if (!latest || item.updateTime > latest.updateTime) {
                latest = item;
            }
        }
        $('#live_running_count').text(runningCount);
        if (latest) {
            updateDashboard(latest.statusText, latest.progress, '策略 #' + latest.strategyId + ' · ' + latest.message);
        }
    }

    function updateDashboard(status, progress, message) {
        if (status != null) {
            $('#live_last_event').text(status);
        }
        if (progress != null && progress !== '') {
            $('#live_global_progress_bar').css('width', progress + '%');
            $('#live_global_progress_text').text(progress + '%');
        }
        if (message != null) {
            $('#live_global_message').text(message);
        }
    }

    function defaultLiveState() {
        return {
            status: 'IDLE',
            statusText: '空闲',
            progress: 0,
            message: '暂无实时运行',
            stageCode: '-',
            current: null,
            total: null,
            runId: null
        };
    }

    function statusText(status) {
        if (status === 'RUNNING') {
            return '运行中';
        }
        if (status === 'SUCCESS') {
            return '成功';
        }
        if (status === 'FAIL') {
            return '失败';
        }
        if (status === 'CONNECTED') {
            return '已连接';
        }
        return '空闲';
    }

    function statusClassName(status) {
        if (status === 'RUNNING') {
            return 'running';
        }
        if (status === 'SUCCESS') {
            return 'success';
        }
        if (status === 'FAIL') {
            return 'fail';
        }
        return 'idle';
    }

    function safe(value) {
        return value == null ? '' : String(value);
    }

    function safeNumber(value) {
        return value == null ? 0 : value;
    }
});
