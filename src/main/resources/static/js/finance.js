$(function () {
    // init date tables
    var financeDetailList = $("#finance_detail_list").dataTable({
        "deferRender": true,
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/financeAnalysis/pageList",
            type: "post",
            data: function (d) {
                var obj = {};
                var condition_report = document.getElementById("condition_report");
                var time_select = condition_report.options[condition_report.selectedIndex].value;

                var condition_order = document.getElementById("condition_order");
                var order_select = condition_order.options[condition_order.selectedIndex].value;

                obj.name = document.getElementById("company_name").innerText;
                obj.stockCode = $('#stock_code').val();
                obj.order = order_select;
                obj.reportTime = time_select;
                obj.start = d.start;
                obj.length = d.length;
                return obj;
            }
        },
        "searching": false,
        "ordering": false,
        //"scrollX": true,	// scroll x，close self-adaption
        "columns": [
            {
                "data": 'stockCompanyId',
                "visible": false,
                "width": '1%'
            },
            {
                "data": 'name',
                "visible": true,
                "width": '5%'
            },
            {
                "data": 'lastPrice',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'totalIncome',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'totalAddPercent',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'netProfit',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'netProfitPercent',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'profitTotalPercent',
                "visible": true,
                "width": '10%'
            },
            {
                "data": 'reportTime',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'reportType',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'updatedTime',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'detail',
                "width": '6%',
                "render": function (data, type, row) {
                    return function () {
                        // html
                        tableData['key' + row.stockCompanyId] = row;
                        var html = '<p id="' + row.stockCompanyId + '" >' +
                            '<button class="btn btn-warning btn-xs detail" type="button">' + "公司详情" + '</button>  ' +
                            '</p>';

                        return html;
                    };
                }
            }
        ],
        "language": {
            "sProcessing": "处理中...",
            "sLengthMenu": "每页 _MENU_ 条记录",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
            "sInfoEmpty": "无记录",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": "以升序排列此列",
                "sSortDescending": "以降序排列此列"
            }
        }
    });

    // table data
    var tableData = {};

    $("#finance_detail_list").on('click', '.detail', function () {
        var id = $(this).parent('p').attr("id");
        window.open(base_url + '/stockDetail?companyStockId=' + id, '_self');
    });

    $('#searchBtn').on('click', function(){
        financeDetailList.fnDraw();
    });

});