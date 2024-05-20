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

                obj.name = $('#company_name').val();
                obj.stockCode = $('#stock_code').val();
                obj.totalCapitalization = $('#totalCapitalization').val();
                obj.totalAddPercent = $('#totalAddPercent').val();
                obj.netProfitPercent = $('#netProfitPercent').val();
                obj.netProfit = $('#netProfit').val();
                obj.bkinfo = $('#bkinfo').val();
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
                "data": 'stockCode',
                "visible" : true,
                "width":'5%'
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
                "data": 'dtzf',
                "visible": true,
                "width": '1%'
            },
            {
                "data": 'dtcjl',
                "visible": true,
                "width": '2%'
            },
            {
                "data": 'dtcjje',
                "visible": true,
                "width": '2%'
            },
            {
                "data": 'dthsl',
                "visible": true,
                "width": '2%'
            },
            {
                "data": 'lb',
                "visible": true,
                "width": '1%'
            },
            {
                "data": 'syl',
                "visible": true,
                "width": '1%'
            },
            {
                "data": 'totalCapitalization',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'totalIncome',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'incomeTotalPercent',
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
                "width": '6%'
            },
            {
                "data": 'bkInfo',
                "visible": true,
                "width": '6%'
            },
            {
                "data": 'conceptInfo',
                "visible": true,
                "width": '12%'
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

    $('#exportBtn').on('click', function(){
        var condition_report = document.getElementById("condition_report");
        var time_select = condition_report.options[condition_report.selectedIndex].value;
        var condition_order = document.getElementById("condition_order");
        var order_select = condition_order.options[condition_order.selectedIndex].value;
        data = {
            name:  document.getElementById("company_name").innerText,
            stockCode: $('#stock_code').val(),
            companyStock: $('#stock_code').val(),
            totalCapitalization: $('#totalCapitalization').val(),
            totalAddPercent: $('#totalAddPercent').val(),
            netProfitPercent: $('#netProfitPercent').val(),
            netProfit:  $('#netProfit').val(),
            bkinfo:  $('#bkinfo').val(),
            order: order_select,
            reportTime: time_select
        };
// Use XMLHttpRequest instead of Jquery $ajax
        xhttp = new XMLHttpRequest();
        var myDate = new Date();
        var myYear = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
        var myMonth = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
        var myToday = myDate.getDate(); //获取当前日(1-31)
        var myHour = myDate.getHours(); //获取当前小时数(0-23)
        var myMinute = myDate.getMinutes(); //获取当前分钟数(0-59)
        var mySecond = myDate.getSeconds(); //获取当前秒数(0-59)
        var nowTime;

        nowTime = myYear+ fillZero(myMonth)+ fillZero(myToday)+ fillZero(myHour)+
            fillZero(myMinute) + fillZero(mySecond);

        xhttp.onreadystatechange = function() {
            var a;
            if (xhttp.readyState === 4 && xhttp.status === 201) {
                // Trick for making downloadable link
                a = document.createElement('a');
                a.href = window.URL.createObjectURL(xhttp.response);
                // Give filename you wish to download
                a.download = "Finance_"+time_select+"_"+nowTime+".xls";
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
            }
        };
// Post data to URL which handles post request
        xhttp.open("POST", base_url + '/financeAnalysis/export');
        xhttp.setRequestHeader("Content-Type", "application/json");
// You should set responseType as blob for binary responses
        xhttp.responseType = 'blob';
        xhttp.timeout = 4000000;
        xhttp.send(JSON.stringify(data));

    });

    function fillZero(str) {
        var realNum;
        if (str < 10) {
            realNum = '0' + str;
        } else {
            realNum = str;
        }
        return realNum;
    }
});