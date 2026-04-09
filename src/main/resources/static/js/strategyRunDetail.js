$(function() {
    var runId = $('#run_id').val();

    $.get(base_url + '/strategyRun/detail', {runId: runId}, function(resp) {
        if (resp.code == 0 && resp.data) {
            var d = resp.data;
            var html = '';
            html += buildCell('运行ID', d.id);
            html += buildCell('策略名称', d.strategyName);
            html += buildCell('策略编码', d.strategyCode);
            html += buildCell('版本号', d.versionNo);
            html += buildCell('运行状态', d.runStatus);
            html += buildCell('运行方式', d.runType);
            html += buildCell('开始时间', d.startTime);
            html += buildCell('结束时间', d.endTime);
            html += buildCell('耗时(ms)', d.durationMs);
            html += buildCell('结果数量', d.resultCount);
            html += buildCell('错误摘要', d.errorMessage || '');
            $('#summary_wrapper').html(html);
        }
    });

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

    $.get(base_url + '/strategyRun/resultList', {runId: runId}, function(resp) {
        if (resp.code == 0 && resp.data) {
            $('#result_panel').text(JSON.stringify(resp.data, null, 2));
            var html = '';
            for (var i = 0; i < resp.data.length; i++) {
                var item = resp.data[i];
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
    });

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

