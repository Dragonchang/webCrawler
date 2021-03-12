<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="./layui/dist/css/layui.css"  media="all">
    <style>
        #report_wrapper{
            background: white;
            overflow: hidden;
        }
        #report_wrapper ul{
            width: 1800px;
        }
        #report_wrapper li{
            width: 100px;
            height: 50px;
            float: left;
            text-align: center;
            line-height: 50px;
            cursor: pointer;
        }
        #report_wrapper li.active{
            background: hotpink;
            color: red;
        }
    </style>

    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- left -->
    <@netCommon.commonLeft />
    <div class="content-wrapper">
        <section class="content-header">
            <h1>${company.name}</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info" id="addBtn">加关注</button>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">股价统计</h3>
                            <div class="pull-right box-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime_avg" >
                                    <i class="fa fa-calendar"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="price_lineChart" style="height: 250px;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">市值统计信息</h3>
                            <div class="pull-right box-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime_total" >
                                    <i class="fa fa-calendar"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="total_lineChart" style="height: 250px;"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">股东信息统计</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12 layui-tab layui-tab-card">
                                    <div id="report_wrapper">
                                        <ul class="layui-tab-title">
                                            <#if holder?exists>
                                                <#list holder?keys as key>
                                                    <#if key_index = 0>
                                                        <li class="layui-this">${key}</li>
                                                    <#else>
                                                        <li>${key}</li>
                                                    </#if >
                                                </#list>
                                            </#if>
                                        </ul>
                                    </div>
                                    <div class="layui-tab-content">
                                        <#if holder?exists>
                                            <#list holder?keys as key>
                                                <#if key_index = 0>
                                                    <div class="layui-tab-item layui-show">
                                                        <table class="layui-table" border="0" cellspacing="1" cellpadding="0">
                                                            <tr class="layui-table-header">
                                                                <td width="105">名次</td>
                                                                <td width="181">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="181">持股数(股)</td>
                                                                <td width="112">占总股本持股比例</td>
                                                                <td width="181">增减(股)</td>
                                                                <td width="112">变动比例</td>
                                                            </tr>
                                                            <#list holder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td width="112">${holder.holderRank}</td>
                                                                    <td width="112">${holder.holderName}</td>
                                                                    <td width="112">${holder.stockType}</td>
                                                                    <td width="112">${holder.holdCount}</td>
                                                                    <td width="112">${holder.holdPercent}</td>
                                                                    <td width="112">${holder.zj}</td>
                                                                    <td width="112">${holder.changePercent}</td>
                                                                </tr>
                                                            </#list>
                                                        </table>
                                                    </div>
                                                <#else>
                                                    <div class="layui-tab-item">
                                                        <table class="layui-table" border="0" cellspacing="1" cellpadding="0">
                                                            <tr class="layui-table-header">
                                                                <td width="105">名次</td>
                                                                <td width="181">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="181">持股数(股)</td>
                                                                <td width="112">占总股本持股比例</td>
                                                                <td width="181">增减(股)</td>
                                                                <td width="112">变动比例</td>
                                                            </tr>
                                                            <#list holder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td width="112">${holder.holderRank}</td>
                                                                    <td width="112">${holder.holderName}</td>
                                                                    <td width="112">${holder.stockType}</td>
                                                                    <td width="112">${holder.holdCount}</td>
                                                                    <td width="112">${holder.holdPercent}</td>
                                                                    <td width="112">${holder.zj}</td>
                                                                    <td width="112">${holder.changePercent}</td>
                                                                </tr>
                                                            </#list>
                                                        </table>
                                                    </div>
                                                </#if >
                                            </#list>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">流通股东信息统计</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12 layui-tab layui-tab-card">
                                    <div id="report_wrapper_LT">
                                        <ul class="layui-tab-title">
                                            <#if ltHolder?exists>
                                                <#list ltHolder?keys as key>
                                                    <#if key_index = 0>
                                                        <li class="layui-this">${key}</li>
                                                    <#else>
                                                        <li>${key}</li>
                                                    </#if >
                                                </#list>
                                            </#if>
                                        </ul>
                                    </div>
                                    <div class="layui-tab-content">
                                        <#if ltHolder?exists>
                                            <#list ltHolder?keys as key>
                                                <#if key_index = 0>
                                                    <div class="layui-tab-item layui-show">
                                                        <table class="layui-table" border="0" cellspacing="1" cellpadding="0">
                                                            <tr class="layui-table-header">
                                                                <td width="105">名次</td>
                                                                <td width="181">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="181">持股数(股)</td>
                                                                <td width="112">占总股本持股比例</td>
                                                                <td width="181">增减(股)</td>
                                                                <td width="112">变动比例</td>
                                                            </tr>
                                                            <#list ltHolder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td width="112">${holder.holderRank}</td>
                                                                    <td width="112">${holder.holderName}</td>
                                                                    <td width="112">${holder.stockType}</td>
                                                                    <td width="112">${holder.holdCount}</td>
                                                                    <td width="112">${holder.holdPercent}</td>
                                                                    <td width="112">${holder.zj}</td>
                                                                    <td width="112">${holder.changePercent}</td>
                                                                </tr>
                                                            </#list>
                                                        </table>
                                                    </div>
                                                <#else>
                                                    <div class="layui-tab-item">
                                                        <table class="layui-table" border="0" cellspacing="1" cellpadding="0">
                                                            <tr class="layui-table-header">
                                                                <td width="105">名次</td>
                                                                <td width="181">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="181">持股数(股)</td>
                                                                <td width="112">占总股本持股比例</td>
                                                                <td width="181">增减(股)</td>
                                                                <td width="112">变动比例</td>
                                                            </tr>
                                                            <#list ltHolder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td width="112">${holder.holderRank}</td>
                                                                    <td width="112">${holder.holderName}</td>
                                                                    <td width="112">${holder.stockType}</td>
                                                                    <td width="112">${holder.holdCount}</td>
                                                                    <td width="112">${holder.holdPercent}</td>
                                                                    <td width="112">${holder.zj}</td>
                                                                    <td width="112">${holder.changePercent}</td>
                                                                </tr>
                                                            </#list>
                                                        </table>
                                                    </div>
                                                </#if >
                                            </#list>
                                        </#if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">资金流统计</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    </div>
    <!-- footer -->
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<!-- daterangepicker -->
<script src="${request.contextPath}/js/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<#-- echarts -->
<script src="${request.contextPath}/js/plugins/echarts/echarts.common.min.js"></script>
<script src="${request.contextPath}/js/iscroll/iscroll.js"></script>
<script src="${request.contextPath}/layui/dist/layui.js"></script>
<script src="${request.contextPath}/js/stockDetail.js"></script>

<script>
    layui.use('element', function(){
        var $ = layui.jquery
            ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
    });
</script>

</body>
</html>