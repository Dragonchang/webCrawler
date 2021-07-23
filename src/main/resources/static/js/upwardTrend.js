$(function() {
    // init date tables
    var companyListTable1 = $("#upward_tend_list1").dataTable({
        "deferRender": true,
        "processing" : true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/recommend/newPageList",
            type:"post",
            data : function ( d ) {
                var newcondition1 = document.getElementById("newcondition1");
                var select1 = newcondition1.options[newcondition1.selectedIndex].value;
                var obj = {};
                obj.filter = select1;
                obj.name = $('#name1').val();
                obj.stockCode = $('#stock_code1').val();
                obj.today = $('#reservation1').val();
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
                "data": 'id',
                "visible" : false,
                "width":'10%'
            },
            {
                "data": 'companyStockId',
                "visible" : false,
                "width":'10%'
            },
            {
                "data": 'stockCode',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'name',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'lastPrice',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'reportTime',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'detail',
                "width":'10%',
                "render": function ( data, type, row ) {
                    return function(){
                        // html
                        tableData['key'+row.companyStockId] = row;
                        var html = '<p id="'+ row.companyStockId +'" >'+
                            '<button class="btn btn-warning btn-xs detail" type="button">'+ "详情" +'</button>  '+
                            '<button class="btn btn-warning btn-xs add" type="button">'+ "加关注" +'</button>  '+
                            '</p>';

                        return html;
                    };
                }
            }
        ],
        "language" : {
            "sProcessing" : "处理中..." ,
            "sLengthMenu" : "每页 _MENU_ 条记录" ,
            "sZeroRecords" : "没有匹配结果" ,
            "sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )" ,
            "sInfoEmpty" : "无记录" ,
            "sInfoFiltered" : "(由 _MAX_ 项结果过滤)" ,
            "sInfoPostFix" : "",
            "sSearch" : "搜索" ,
            "sUrl" : "",
            "sEmptyTable" : "表中数据为空" ,
            "sLoadingRecords" : "载入中..." ,
            "sInfoThousands" : ",",
            "oPaginate" : {
                "sFirst" : "首页" ,
                "sPrevious" : "上页" ,
                "sNext" : "下页" ,
                "sLast" : "末页"
            },
            "oAria" : {
                "sSortAscending" : "以升序排列此列" ,
                "sSortDescending" : "以降序排列此列"
            }
        }
    });

    $('#reservation1').daterangepicker({
            autoUpdateInput: false,
            autoclose: true,
            singleDatePicker:true,
            maxDate: moment(new Date()), //设置最大日期
            "opens": "center",
            format: 'YYYY-MM-DD',
            ranges: {
                '今天': [moment()],
                '昨天': [moment().subtract(1, 'days')],
                '上周': [moment().subtract(6, 'days')],
                '最近一月': [moment().subtract(1, 'months').startOf('day')],
                '最近六月': [moment().subtract(6, 'months').startOf('day')],
                '最近一年月': [moment().subtract(12, 'months').startOf('day')]
            },
            locale: {
                format: "YYYY-MM-DD",
            }
        },
        function (start, end) {
            $('#reservation1').data('daterangepicker').autoUpdateInput=true
        }
    )

    $('#clear1').on('click', function(){
        $('#reservation1').val('');
    })

    // search btn
    $('#searchBtn1').on('click', function(){
        companyListTable1.fnDraw();
    });

    $('#exportBtn1').on('click', function(){
        var newcondition1 = document.getElementById("newcondition1");
        var select1 = newcondition1.options[newcondition1.selectedIndex].value;
        data = {
            filter: select1,
            name: $('#name1').val(),
            stockCode: $('#stock_code1').val(),
            today: $('#reservation1').val()
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
                a.download = "upwardTrend"+"_"+nowTime+".xls";
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
            }
        };
// Post data to URL which handles post request
        xhttp.open("POST", base_url + '/recommend/newExport');
        xhttp.setRequestHeader("Content-Type", "application/json");
// You should set responseType as blob for binary responses
        xhttp.responseType = 'blob';
        xhttp.send(JSON.stringify(data));

    });

    // job operate
    $("#upward_tend_list1").on('click', '.detail',function() {
        var id = $(this).parent('p').attr("id");
        window.open(base_url + '/stockDetail?companyStockId=' + id, '_self');
    });

    $("#upward_tend_list1").on('click', '.add',function() {
        var id = $(this).parent('p').attr("id");
        var row = tableData['key'+id];
        var dataValue = {
            stockCompanyId: id,
            stockCode: row.stockCode,
            companyName: row.name,
            type: "1"
        };
        $.ajax({
            type : 'POST',
            dataType: 'json' ,
            contentType: "application/json" ,
            url : base_url + '/companyFocus/add',
            data : JSON.stringify(dataValue),
            dataType : "json",
            success : function(data){
                if (data.code == 0) {
                } else {
                }
            }
        });
    });

    /************************************************************************************************************************/
    // init date tables
    var companyListTable = $("#upward_tend_list").dataTable({
        "deferRender": true,
        "processing" : true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/recommend/pageList",
            type:"post",
            data : function ( d ) {
                var condition1 = document.getElementById("condition1");
                var select1 = condition1.options[condition1.selectedIndex].value;
                var condition2 = document.getElementById("condition2");
                var select2 = condition2.options[condition2.selectedIndex].value;
                var obj = {};
                obj.filter = select1;
                obj.isHeight = select2;
                obj.name = $('#name').val();
                obj.stockCode = $('#stock_code').val();
                obj.today = $('#reservation').val();
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
                "data": 'id',
                "visible" : false,
                "width":'10%'
            },
            {
                "data": 'companyStockId',
                "visible" : false,
                "width":'10%'
            },
            {
                "data": 'stockCode',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'name',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'lastPrice',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'avgFive',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'avgTen',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'avgTwenty',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'avgThirty',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'avgSixty',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'avgNinety',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'avgHundtwenty',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'reportTime',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'detail',
                "width":'10%',
                "render": function ( data, type, row ) {
                    return function(){
                        // html
                        tableData['key'+row.companyStockId] = row;
                        var html = '<p id="'+ row.companyStockId +'" >'+
                            '<button class="btn btn-warning btn-xs detail" type="button">'+ "详情" +'</button>  '+
                            '<button class="btn btn-warning btn-xs add" type="button">'+ "加关注" +'</button>  '+
                            '</p>';

                        return html;
                    };
                }
            }
        ],
        "language" : {
            "sProcessing" : "处理中..." ,
            "sLengthMenu" : "每页 _MENU_ 条记录" ,
            "sZeroRecords" : "没有匹配结果" ,
            "sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )" ,
            "sInfoEmpty" : "无记录" ,
            "sInfoFiltered" : "(由 _MAX_ 项结果过滤)" ,
            "sInfoPostFix" : "",
            "sSearch" : "搜索" ,
            "sUrl" : "",
            "sEmptyTable" : "表中数据为空" ,
            "sLoadingRecords" : "载入中..." ,
            "sInfoThousands" : ",",
            "oPaginate" : {
                "sFirst" : "首页" ,
                "sPrevious" : "上页" ,
                "sNext" : "下页" ,
                "sLast" : "末页"
            },
            "oAria" : {
                "sSortAscending" : "以升序排列此列" ,
                "sSortDescending" : "以降序排列此列"
            }
        }
    });

    $('#reservation').daterangepicker({
            autoUpdateInput: false,
            autoclose: true,
            singleDatePicker:true,
            maxDate: moment(new Date()), //设置最大日期
            "opens": "center",
            format: 'YYYY-MM-DD',
            ranges: {
                '今天': [moment()],
                '昨天': [moment().subtract(1, 'days')],
                '上周': [moment().subtract(6, 'days')],
                '最近一月': [moment().subtract(1, 'months').startOf('day')],
                '最近六月': [moment().subtract(6, 'months').startOf('day')],
                '最近一年月': [moment().subtract(12, 'months').startOf('day')]
            },
            locale: {
                format: "YYYY-MM-DD",
            }
        },
        function (start, end) {
            $('#reservation').data('daterangepicker').autoUpdateInput=true
        }
    )

    $('#clear').on('click', function(){
        $('#reservation').val('');
    })
    // table data
    var tableData = {};

    // search btn
    $('#searchBtn').on('click', function(){
        companyListTable.fnDraw();
    });

    $('#exportBtn').on('click', function(){
        var condition1 = document.getElementById("condition1");
        var select1 = condition1.options[condition1.selectedIndex].value;
        var condition2 = document.getElementById("condition2");
        var select2 = condition2.options[condition2.selectedIndex].value;
        data = {
            filter: select1,
            isHeight: select2,
            name: $('#name').val(),
            stockCode: $('#stock_code').val(),
            today: $('#reservation').val()
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
                a.download = "upwardTrend"+"_"+nowTime+".xls";
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
            }
        };
// Post data to URL which handles post request
        xhttp.open("POST", base_url + '/recommend/export');
        xhttp.setRequestHeader("Content-Type", "application/json");
// You should set responseType as blob for binary responses
        xhttp.responseType = 'blob';
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

    // job operate
    $("#upward_tend_list").on('click', '.detail',function() {
        var id = $(this).parent('p').attr("id");
        window.open(base_url + '/stockDetail?companyStockId=' + id, '_self');
    });

    $("#upward_tend_list").on('click', '.add',function() {
        var id = $(this).parent('p').attr("id");
        var row = tableData['key'+id];
        var dataValue = {
            stockCompanyId: id,
            stockCode: row.stockCode,
            companyName: row.name,
            type: "1"
        };
        $.ajax({
            type : 'POST',
            dataType: 'json' ,
            contentType: "application/json" ,
            url : base_url + '/companyFocus/add',
            data : JSON.stringify(dataValue),
            dataType : "json",
            success : function(data){
                if (data.code == 0) {
                } else {
                }
            }
        });
    });
});
