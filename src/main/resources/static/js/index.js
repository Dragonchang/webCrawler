$(function () {
    // filter Time
    var rangesConf = {};
    rangesConf["最近一周"] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf["最近一月"] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];
    rangesConf["最近二月"] = [moment().subtract(2, 'months').startOf('day'), moment().endOf('day')];
    rangesConf["最近三个月"] = [moment().subtract(3, 'months').startOf('day'), moment().endOf('day')];
    rangesConf["最近一年"] = [moment().subtract(12, 'months').startOf('day'), moment().endOf('day')];

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
        startDate: rangesConf["最近一年"][0],
        endDate: rangesConf["最近一年"][1]
    }, function (start, end, label) {
        freshChartDate(start, end);
    });

    freshChartDate(rangesConf["最近一年"][0], rangesConf["最近一年"][1]);
    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshChartDate(startDate, endDate) {
        $.ajax({
            type : 'POST',
            url : base_url + '/avgChartInfo',
            data : {
                'startDate':startDate.format('YYYY-MM-DD HH:mm:ss'),
                'endDate':endDate.format('YYYY-MM-DD HH:mm:ss')
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
                   text: "股价日期分布图"
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
                       data : data.data.dayList
                   }
               ],
               yAxis : [
                   {
                       type : 'value'
                   }
               ],
               series : [
                   {
                       name:"股价",
                       type:'line',
                       data: data.data.avgPriceList
                   }
               ],
                color:['#00A65A']
        };

        var lineChart = echarts.init(document.getElementById('avg_lineChart'));
        lineChart.setOption(option);
    }


    $('#filterTime_total').daterangepicker({
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
        startDate: rangesConf["最近一年"][0],
        endDate: rangesConf["最近一年"][1]
    }, function (start, end, label) {
        freshTotalChartDate(start, end);
    });

    freshTotalChartDate(rangesConf["最近一年"][0], rangesConf["最近一年"][1]);
    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshTotalChartDate(startDate, endDate) {
        $.ajax({
            type : 'POST',
            url : base_url + '/totalChartInfo',
            data : {
                'startDate':startDate.format('YYYY-MM-DD HH:mm:ss'),
                'endDate':endDate.format('YYYY-MM-DD HH:mm:ss')
            },
            dataType : "json",
            success : function(data){
                if (data.code == 0) {
                    totalLineChartInit(data)
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
    function totalLineChartInit(data) {
        var option = {
            title: {
                text: "市值日期分布图"
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
                data:["总市值","总流通市值"]
            },
            calculable : true,
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
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data.data.dayList
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:"总市值",
                    type:'line',
                    data: data.data.totalCapitalizationList
                },
                {
                    name:"总流通市值",
                    type:'line',
                    data: data.data.lastCirculation
                }
            ],
            color:['#c23632', '#F39C12']
        };

        var lineChart = echarts.init(document.getElementById('total_lineChart'));
        lineChart.setOption(option);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    $('#filterTime_income_profit').daterangepicker({
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
        startDate: rangesConf["最近一年"][0],
        endDate: rangesConf["最近一年"][1]
    }, function (start, end, label) {
        freshIncomeProfitChartDate(start, end);
    });

    $('#queryBtn').on('click', function(){
        freshIncomeProfitChartDate();
    });

    freshIncomeProfitChartDate();
    /**
     * fresh Chart Date
     *
     * @param startDate
     * @param endDate
     */
    function freshIncomeProfitChartDate() {
        $.ajax({
            type : 'POST',
            url : base_url + '/incomeProfit',
            data : {
                'timeSelect':data_select.options[data_select.selectedIndex].value
            },
            dataType : "json",
            success : function(data){
                if (data.code == 0) {
                    incomeProfitLineChartInit(data)
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
    function incomeProfitLineChartInit(data) {
        var option = {
            title: {
                text: "营收利润日期分布图"
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
                data:["营收","利润"]
            },
            calculable : true,
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
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : data.data.dayList
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:"统计个数",
                    type:'line',
                    data: data.data.count
                },
                {
                    name:"总营收(亿)",
                    type:'line',
                    data: data.data.incomeList
                },
                {
                    name:"总利润(亿)",
                    type:'line',
                    data: data.data.profitList
                }
            ],
            color:['#c23632', '#F39C12', '#F32288']
        };

        var lineChart = echarts.init(document.getElementById('income_profit_lineChart'));
        lineChart.setOption(option);
    }
});
