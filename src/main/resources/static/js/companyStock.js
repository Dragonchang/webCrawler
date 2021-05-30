$(function() {
    // init date tables
    var companyListTable = $("#company_stock_list").dataTable({
        "deferRender": true,
        "processing" : true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/companyStock/pageList",
            type:"post",
            data : function ( d ) {
                var obj = {};
                var condition = document.getElementById("condition");
                var select = condition.options[condition.selectedIndex].value;
                obj.order = select;
                obj.name = $('#name').val();
                obj.stockCode = $('#stock_code').val();
                obj.start = d.start;
                obj.length = d.length;
                obj.marketTime = $('#reservation').val();
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
                "data": 'companyId',
                "visible" : false,
                "width":'10%'
            },
            {
                "data": 'name',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'stockCode',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'lastPrice',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'totalCapitalization',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'lastCirculation',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'lastIncome',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'marketTime',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'updatedTime',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'detail',
                "width":'10%',
                "render": function ( data, type, row ) {
                    return function(){
                        // html
                        tableData['key'+row.id] = row;
                        var html = '<p id="'+ row.id +'" >'+
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
         maxDate: moment(new Date()), //设置最大日期
         "opens": "center",
          ranges: {
                '今天': [moment(), moment()],
                '昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '上周': [moment().subtract(6, 'days'), moment()],
                '最近一月': [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')],
                '最近六月': [moment().subtract(6, 'months').startOf('day'), moment().endOf('day')],
                '最近一年月': [moment().subtract(12, 'months').startOf('day'), moment().endOf('day')]
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
        var condition = document.getElementById("condition");
        var select = condition.options[condition.selectedIndex].value;
        data = {
            order: select,
            name: $('#name').val(),
            stockCode: $('#stock_code').val(),
            marketTime: $('#reservation').val()
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
                a.download = "Company"+"_"+nowTime+".xls";
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
            }
        };
// Post data to URL which handles post request
        xhttp.open("POST", base_url + '/companyStock/export');
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
    $("#company_stock_list").on('click', '.detail',function() {
        var id = $(this).parent('p').attr("id");
        window.open(base_url + '/stockDetail?companyStockId=' + id, '_self');
    });

    $("#company_stock_list").on('click', '.add',function() {
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
