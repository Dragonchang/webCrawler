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
                    <span id="stockCompanyId" style="display:none;">${company.id?c}</span>
                    <span id="stockCode" style="display:none;">${company.stockCode}</span>
                    <span id="companyName" style="display:none;">${company.name}</span>
                    <button class="btn btn-block btn-info" id="addBtn">加关注</button>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">公司K线</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="price_lineChart" style="height: 700px;"></div>
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
                                                                <td width="281">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="100">持股数(股)</td>
                                                                <td width="50">占总股本持股比例</td>
                                                                <td width="100">增减(股)</td>
                                                                <td width="50">变动比例</td>
                                                            </tr>
                                                            <#list holder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td>${holder.holderRank}</td>
                                                                    <td class="holder_name" style= "cursor:pointer; color: #00ca6d">${holder.holderName}</td>
                                                                    <td>${holder.stockType}</td>
                                                                    <td>${holder.holdCount}</td>
                                                                    <td>${holder.holdPercent}</td>
                                                                    <td>${holder.zj}</td>
                                                                    <td>${holder.changePercent}</td>
                                                                </tr>
                                                            </#list>
                                                        </table>
                                                    </div>
                                                <#else>
                                                    <div class="layui-tab-item">
                                                        <table class="layui-table" border="0" cellspacing="1" cellpadding="0">
                                                            <tr class="layui-table-header">
                                                                <td width="105">名次</td>
                                                                <td width="281">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="100">持股数(股)</td>
                                                                <td width="50">占总股本持股比例</td>
                                                                <td width="100">增减(股)</td>
                                                                <td width="50">变动比例</td>
                                                            </tr>
                                                            <#list holder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td>${holder.holderRank}</td>
                                                                    <td class="holder_name" style= "cursor:pointer; color: #00ca6d">${holder.holderName}</td>
                                                                    <td>${holder.stockType}</td>
                                                                    <td>${holder.holdCount}</td>
                                                                    <td>${holder.holdPercent}</td>
                                                                    <td>${holder.zj}</td>
                                                                    <td>${holder.changePercent}</td>
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
                                                                <td width="281">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="100">持股数(股)</td>
                                                                <td width="50">占总股本持股比例</td>
                                                                <td width="100">增减(股)</td>
                                                                <td width="50">变动比例</td>
                                                            </tr>
                                                            <#list ltHolder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td>${holder.holderRank}</td>
                                                                    <td class="holder_name" style= "cursor:pointer; color: #00ca6d">${holder.holderName}</td>
                                                                    <td>${holder.stockType}</td>
                                                                    <td>${holder.holdCount}</td>
                                                                    <td>${holder.holdPercent}</td>
                                                                    <td>${holder.zj}</td>
                                                                    <td>${holder.changePercent}</td>
                                                                </tr>
                                                            </#list>
                                                        </table>
                                                    </div>
                                                <#else>
                                                    <div class="layui-tab-item">
                                                        <table class="layui-table" border="0" cellspacing="1" cellpadding="0">
                                                            <tr class="layui-table-header">
                                                                <td width="105">名次</td>
                                                                <td width="281">股东名称</td>
                                                                <td width="112">股份类型</td>
                                                                <td width="100">持股数(股)</td>
                                                                <td width="50">占总股本持股比例</td>
                                                                <td width="100">增减(股)</td>
                                                                <td width="50">变动比例</td>
                                                            </tr>
                                                            <#list ltHolder[key] as holder>
                                                                <tr  class="layui-table-body">
                                                                    <td>${holder.holderRank}</td>
                                                                    <td class="holder_name" style= "cursor:pointer; color: #00ca6d">${holder.holderName}</td>
                                                                    <td>${holder.stockType}</td>
                                                                    <td>${holder.holdCount}</td>
                                                                    <td>${holder.holdPercent}</td>
                                                                    <td>${holder.zj}</td>
                                                                    <td>${holder.changePercent}</td>
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

<script src="${request.contextPath}/js/iscroll/iscroll.js"></script>
<script src="${request.contextPath}/layui/dist/layui.js"></script>

<#-- echarts -->
<script src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="${request.contextPath}/js/Kecharts.js"></script>
<script src="${request.contextPath}/js/stockDetail.js"></script>

<script>
    layui.use('element', function(){
        var $ = layui.jquery
            ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
    });
</script>

</body>
</html>