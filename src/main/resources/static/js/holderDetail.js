$(function () {
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
            url : base_url + '/stockDetail/countChartInfo',
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
