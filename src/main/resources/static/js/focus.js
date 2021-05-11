$(function() {
    // init date tables
    var companyListTable = $("#company_list").dataTable({
        "deferRender": true,
        "processing" : true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/companyFocus/pageList",
            type:"post",
            data : function ( d ) {
                var obj = {};
                var condition = document.getElementById("condition");
                var select = condition.options[condition.selectedIndex].value;
                obj.type = select;
                obj.companyName = $('#company_name').val();
                obj.stockCode = $('#stock_code').val();
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
                "data": 'stockCompanyId',
                "visible" : false,
                "width":'10%'
            },
            {
                "data": 'stockCode',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'companyName',
                "visible" : true,
                "width":'20%'
            },
            {
                "data": 'type',
                "visible" : true,
                "width":'20%'
            },
            {
                "data": 'createdTime',
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
                            '<button class="btn btn-warning btn-xs delete" type="button">'+ "删除" +'</button>  '+
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

    // table data
    var tableData = {};

    // search btn
    $('#searchBtn').on('click', function(){
        companyListTable.fnDraw();
    });

    // job operate
    $("#company_list").on('click', '.detail',function() {
        var id = $(this).parent('p').attr("id");
        var row = tableData['key'+id];
        var companyStockId = row.stockCompanyId;
        var name = row.companyName;
        var type = row.type;
        if(type == "股份公司") {
            window.open(base_url + '/stockDetail?companyStockId=' + companyStockId, '_self');
        } else if(type == "机构"){
            window.open(base_url + '/stockDetail/getDetail?name=' + name, '_self');
        } else if(type == "个人") {
            window.open(base_url + '/stockDetail/getDetail?name=' + name, '_self');
        } else {
            alert("未知关注类型"+$(this).parent('p').attr("type"))
        }
    });

    // job operate
    $("#company_list").on('click', '.delete',function() {
        var id = $(this).parent('p').attr("id");
        var paramData = {
            "id": id
        };

        $.get(base_url + "/companyFocus/delete", paramData, function(data, status) {
            companyListTable.fnDraw();
        });
    });

});
