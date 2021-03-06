$(function () {
    // init date tables
    var holderDetailList = $("#holder_detail_list").dataTable({
        "deferRender": true,
        "processing" : true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/holderDetail/pageList",
            type:"post",
            data : function ( d ) {
                var obj = {};
                var condition_time = document.getElementById("condition_time");
                var time_select = condition_time.options[condition_time.selectedIndex].value;
                var condition_report = document.getElementById("condition_report");
                var report_select = condition_report.options[condition_report.selectedIndex].value;
                var condition_change = document.getElementById("condition_change");
                var change_select = condition_change.options[condition_change.selectedIndex].value;

                obj.name = document.getElementById("holderName").innerText;
                obj.companyName = $('#company_name').val();
                obj.companyStock = $('#stock_code').val();
                obj.count = time_select;
                obj.change = change_select;
                obj.reportTime = report_select;
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
                "visible" : false,
                "width":'1%'
            },
            {
                "data": 'name',
                "visible" : true,
                "width":'5%'
            },
            {
                "data": 'stockCode',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'lastPrice',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'lastCirculation',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'totalCapitalization',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'lastIncome',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'holderRank',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'holderName',
                "visible" : true,
                "width":'10%'
            },
            {
                "data": 'holdCount',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'holdPercent',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'zj',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'changePercent',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'holderType',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'createdTime',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'reportTime',
                "visible" : true,
                "width":'6%'
            },
            {
                "data": 'detail',
                "width":'6%',
                "render": function ( data, type, row ) {
                    return function(){
                        // html
                        tableData['key'+row.stockCompanyId] = row;
                        var html = '<p id="'+ row.stockCompanyId +'" >'+
                            '<button class="btn btn-warning btn-xs detail" type="button">'+ "公司详情" +'</button>  '+
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

    $("#holder_detail_list").on('click', '.detail',function() {
        var id = $(this).parent('p').attr("id");
        window.open(base_url + '/stockDetail?companyStockId=' + id, '_self');
    });

    // search btn
    $('#searchBtn').on('click', function(){
        holderDetailList.fnDraw();
    });
    $('#exportBtn').on('click', function(){
// Data to post
        var condition_time = document.getElementById("condition_time");
        var time_select = condition_time.options[condition_time.selectedIndex].value;
        var condition_report = document.getElementById("condition_report");
        var report_select = condition_report.options[condition_report.selectedIndex].value;
        var condition_change = document.getElementById("condition_change");
        var change_select = condition_change.options[condition_change.selectedIndex].value;

        data = {
            name:  document.getElementById("holderName").innerText,
            companyName: $('#company_name').val(),
            companyStock: $('#stock_code').val(),
            count: time_select,
            reportTime: report_select,
            change: change_select
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
                a.download = document.getElementById("holderName").innerText+nowTime+".xls";
                a.style.display = 'none';
                document.body.appendChild(a);
                a.click();
            }
        };
// Post data to URL which handles post request
        xhttp.open("POST", base_url + '/holderDetail/export');
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

    // filter Time
    var rangesConf = {};
    rangesConf["最近一周"] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf["最近一月"] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];
    rangesConf["最近二月"] = [moment().subtract(2, 'months').startOf('day'), moment().endOf('day')];
    rangesConf["最近三个月"] = [moment().subtract(3, 'months').startOf('day'), moment().endOf('day')];

    $('#filterTime_avg').daterangepicker({
        autoApply: false,
        singleDatePicker: false,
        showDropdowns: false,        // 是否显示年月选择条件
        timePicker: true, 			// 是否显示小时和分钟选择条件
        timePickerIncrement: 10, 	// 时间的增量，单位为分钟
        timePicker24Hour: true,
        opens: 'left', //日期选择框的弹出位置
        ranges: rangesConf,
        locale: {
            format: 'YYYY-MM-DD HH:mm:ss',
            separator: ' - ',
            customRangeLabel: '自定义',
            applyLabel: '确定',
            cancelLabel: '取消',
            fromLabel: '起始时间',
            toLabel: '结束时间',
            daysOfWeek: '日,一,二,三,四,五,六'.split(','),        // '日', '一', '二', '三', '四', '五', '六'
            monthNames: '一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月'.split(','),        // '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'
            firstDay: 1
        },
        startDate: rangesConf["最近一周"][0],
        endDate: rangesConf["最近一周"][1]
    }, function (start, end, label) {
        freshChartDate(start, end, document.getElementById("holderName").innerText);
    });

    freshChartDate(rangesConf["最近一周"][0], rangesConf["最近一周"][1], document.getElementById("holderName").innerText);
    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshChartDate(startDate, endDate,  name) {
        $.ajax({
            type : 'POST',
            url : base_url + '/holderDetail/countChartInfo',
            data : {
                'startDate':startDate.format('YYYY-MM-DD HH:mm:ss'),
                'endDate':endDate.format('YYYY-MM-DD HH:mm:ss'),
                'name':name
            },
            dataType : "json",
            success : function(data){
                if (data.code == 0) {
                    avgLineChartInit(data)
                } else {
                    alert("获取统计信息失败")
                    // layer.open({
                    //     title: I18n.system_tips ,
                    //     btn: [ I18n.system_ok ],
                    //     content: (data.msg || I18n.job_dashboard_report_loaddata_fail ),
                    //     icon: '2'
                    // });
                }
            }
        });
    }

    /**
     * 平均股价 line Chart Init
     */
    function avgLineChartInit(data) {
        var option = {
            title: {
                text: "持有公司个数统计分布图"
            },
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#6a7985'
                    }
                }
            },
            legend: {
                data:["股价"]
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data.data.reportTime
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:"个数",
                    type:'line',
                    data: data.data.holderCount
                }
            ],
            color:['#00A65A']
        };

        var lineChart = echarts.init(document.getElementById('avg_lineChart'));
        lineChart.setOption(option);
    }

    $('#addBtn').on('click', function(){
        var focusType = "2";
        var holderName = document.getElementById("holderName").innerText;
        if(holderName.length <4) {
            focusType = "3";
        }
        var dataValue = {
            stockCode:  "无",
            companyName: holderName,
            type: focusType
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
