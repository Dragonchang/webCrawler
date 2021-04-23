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

    // job operate
    $("#company_stock_list").on('click', '.detail',function() {
        var id = $(this).parent('p').attr("id");
        window.open(base_url + '/stockDetail?companyStockId=' + id, '_self');
    });

    $("#company_stock_list").on('click', '.add',function() {
        var id = $(this).parent('p').attr("id");

        var paramData = {
            "companyId": id
        };

        // $.get(base_url + "/companyFocus/syncShare", paramData, function(data, status) {
        //     if (data.code == "200") {
        //         //alert("success");
        //     } else {
        //         //alert("failed")
        //     }
        // });
    });


});
