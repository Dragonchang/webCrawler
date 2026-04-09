$(function() {
    var runTable = $('#strategy_run_list').dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + '/strategyRun/pageList',
            type: 'post',
            data: function(d) {
                return {
                    strategyName: $('#strategy_name').val(),
                    runStatus: $('#run_status').val(),
                    start: d.start,
                    length: d.length
                };
            }
        },
        "searching": false,
        "ordering": false,
        "columns": [
            {"data": 'id'},
            {"data": 'strategyName'},
            {"data": 'strategyCode'},
            {"data": 'versionNo'},
            {"data": 'runType'},
            {"data": 'runStatus'},
            {"data": 'startTime'},
            {"data": 'endTime'},
            {"data": 'durationMs'},
            {"data": 'resultCount'},
            {"data": 'errorMessage'},
            {"data": 'detail', "render": function(data, type, row){
                return '<button class="btn btn-primary btn-xs detail" data-id="' + row.id + '">详情</button>';
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
        runTable.fnDraw();
    });

    $('#strategy_run_list').on('click', '.detail', function(){
        var runId = $(this).data('id');
        window.open(base_url + '/strategyRun/detailPage?runId=' + runId, '_self');
    });
});

