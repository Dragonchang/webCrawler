$(function () {
    var paramData = {
        "companyStockId": parseInt(document.getElementById("stockCompanyId").innerText)
    };

    $.get(base_url + "/stockDetail/getPriceRecord", paramData, function(data, status) {
        var rawData = data;

        /*基于准备好的dom，初始化echarts实例*/
        var myChart = echarts.init(document.getElementById('price_lineChart'));
        function calculateMA(dayCount, data) {
            var result = [];
            for (var i = 0, len = data.length; i < len; i++) {
                if (i < dayCount) {
                    result.push('-');
                    continue;
                }
                var sum = 0;
                for (var j = 0; j < dayCount; j++) {
                    sum += data[i - j][1];
                }
                result.push(sum / dayCount);
            }
            return result;
        }


        var dates = rawData.map(function (item) {
            return item[0];
        });

        var priceData = rawData.map(function (item) {
            return [+item[1], +item[2], +item[3], +item[4]];
        });
        var option = {
            legend: {
                data: ['日K', 'MA5', 'MA10', 'MA20', 'MA30'],
                inactiveColor: '#777',
                textStyle: {
                    color: '#000'
                }
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    animation: false,
                    type: 'cross',
                    lineStyle: {
                        color: '#376df4',
                        width: 2,
                        opacity: 1
                    }
                }
            },
            xAxis: {
                type: 'category',
                data: dates,
                axisLine: { lineStyle: { color: '#8392A5' } }
            },
            yAxis: {
                scale: true,
                // interval:20, //值之间的间隔
                axisLine: { lineStyle: { color: '#8392A5' } },
                splitLine: { show: false }
            },
            grid: {
                bottom: 80
            },
            dataZoom: [{
                textStyle: {
                    color: '#8392A5'
                },
                handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                handleSize: '80%',
                dataBackground: {
                    areaStyle: {
                        color: '#8392A5'
                    },
                    lineStyle: {
                        opacity: 0.8,
                        color: '#8392A5'
                    }
                },
                handleStyle: {
                    color: '#fff',
                    shadowBlur: 3,
                    shadowColor: 'rgba(0, 0, 0, 0.6)',
                    shadowOffsetX: 2,
                    shadowOffsetY: 2
                }
            }, {
                type: 'inside'
            }],
            animation: false,
            series: [
                {
                    type: 'candlestick',
                    name: '日K',
                    data: priceData,
                    itemStyle: {
                        normal: {
                            color: '#FD1050',
                            color0: '#0CF49B',
                            borderColor: '#FD1050',
                            borderColor0: '#0CF49B'
                        }
                    }
                },
                {
                    name: 'MA5',
                    type: 'line',
                    data: calculateMA(5, priceData),
                    smooth: true,
                    showSymbol: false,
                    lineStyle: {
                        normal: {
                            width: 1
                        }
                    }
                },
                {
                    name: 'MA10',
                    type: 'line',
                    data: calculateMA(10, priceData),
                    smooth: true,
                    showSymbol: false,
                    lineStyle: {
                        normal: {
                            width: 1
                        }
                    }
                },
                {
                    name: 'MA20',
                    type: 'line',
                    data: calculateMA(20, priceData),
                    smooth: true,
                    showSymbol: false,
                    lineStyle: {
                        normal: {
                            width: 1
                        }
                    }
                },
                {
                    name: 'MA30',
                    type: 'line',
                    data: calculateMA(30, priceData),
                    smooth: true,
                    showSymbol: false,
                    lineStyle: {
                        normal: {
                            width: 1
                        }
                    }
                }
            ]
        };

        myChart.setOption(option);
    });























    new IScroll('#report_wrapper',{
        scrollbars: true,
        scrollX: true
        // 横向滚动导航栏
    });

    new IScroll('#report_wrapper_LT',{
        scrollbars: true,
        scrollX: true
        // 横向滚动导航栏
    });

    $(".holder_name").on('click',function() {
        var name = $(this).text();
        window.open(base_url + '/holderDetail/getDetail?name=' + name, '_self');
    });

    $('#addBtn').on('click', function(){
        var id = parseInt(document.getElementById("stockCompanyId").innerText);
        var code = document.getElementById("stockCode").innerText;
        var name = document.getElementById("companyName").innerText;
        var dataValue = {
            stockCompanyId: id,
            stockCode: code,
            companyName: name,
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
