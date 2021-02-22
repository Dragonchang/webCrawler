$(function () {
    // search btn
    $('#searchBtn').on('click', function () {
        var id = document.getElementById("company_id").innerText;
        var condition = document.getElementById("condition");
        var select = condition.options[condition.selectedIndex].value;
        if(select == 2) {
            window.open(base_url + '/shareCompany?companyId=' + id + '&filterTime='+$('#filterTime').val()+'&orderByPercent=1', '_self');
        } else {
            window.open(base_url + '/shareCompany?companyId=' + id + '&filterTime='+$('#filterTime').val(), '_self');
        }
    });

    // filter Time
    var rangesConf = {};
    rangesConf['今日'] = [moment().startOf('day'), moment().endOf('day')];
    rangesConf["昨日"] = [moment().subtract(1, 'days').startOf('day'), moment().subtract(1, 'days').endOf('day')];
    rangesConf["本月"] = [moment().startOf('month'), moment().endOf('month')];
    rangesConf["上个月"] = [moment().subtract(1, 'months').startOf('month'), moment().subtract(1, 'months').endOf('month')];
    rangesConf["最近一周"] = [moment().subtract(1, 'weeks').startOf('day'), moment().endOf('day')];
    rangesConf["最近一月"] = [moment().subtract(1, 'months').startOf('day'), moment().endOf('day')];

    $('#filterTime').daterangepicker({
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
        startDate: rangesConf["今日"][0],
        endDate: rangesConf["今日"][1]
    });

});
