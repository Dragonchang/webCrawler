$(function() {
    var tableData = {};
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
            {"data": 'publishedVersionNo'},
            {"data": 'status', "render": function(data){ return data == 1 ? '启用' : (data == 2 ? '停用' : '草稿'); }},
            {"data": 'scheduleType'},
            {"data": 'lastRunStatus'},
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
        $.ajax({
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: base_url + '/strategyRun/execute',
            data: JSON.stringify({strategyId: parseInt(id), params: params}),
            success: function(resp){
                if (resp.code == 0) {
                    alert('运行成功，结果数量=' + resp.data.resultCount);
                    window.open(base_url + '/strategyRun/detailPage?runId=' + resp.data.runId, '_self');
                } else {
                    alert(resp.message || '运行失败');
                }
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
});

