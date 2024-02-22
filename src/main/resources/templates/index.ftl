<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- left -->
    <@netCommon.commonLeft />
    <div class="content-wrapper">
        <section class="content-header">
            <h1>统计信息</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-lg-12">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">平均股价统计信息</h3>
                            <div class="pull-right box-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime_avg" >
                                    <i class="fa fa-calendar"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="avg_lineChart" style="height: 350px;"></div>
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
                            <h3 class="box-title">总市值统计信息</h3>
                            <div class="pull-right box-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime_total" >
                                    <i class="fa fa-calendar"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="total_lineChart" style="height: 350px;"></div>
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
                            <h3 class="box-title">企业营收利润信息统计-时间维度</h3>
                            <div class="pull-right box-tools">
                                <div class="col-xs-2  pull-right">
                                    <div class="input-group  pull-right">
                                        <span class="input-group-addon">季度</span>
                                        <select class="form-control" id="data_select" >
                                            <option value="1" >${"第一季度"}</option>
                                            <option value="2" >${"第二季度"}</option>
                                            <option value="3" >${"第三季度"}</option>
                                            <option value="4" selected >${"第四季度"}</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-xs-2 pull-right">
                                    <button class="btn btn-block btn-info " id="queryBtn">查询</button>
                                </div>
                            </div>

                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="income_profit_lineChart" style="height: 350px;"></div>
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
                            <h3 class="box-title">企业营收利润信息统计-板块维度</h3>
                            <div class="pull-right box-tools">
                                <button type="button" class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" id="filterTime_income_profit_bk" >
                                    <i class="fa fa-calendar"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-12">
                                    <div id="income_profit_lineChart_bk" style="height: 350px;"></div>
                                </div>
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
<script src="${request.contextPath}/js/index.js"></script>
</body>
</html>