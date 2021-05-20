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
            <h1>公司价值分析</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">发布时间</span>
                        <select class="form-control" id="condition_report" >
                            <#list reportTime as time>
                                <option value=${time!} >${time!}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">排序方式</span>
                        <select class="form-control" id="condition_order" >
                            <option value="1" >${"营收额"}</option>
                            <option value="2" >${"扣非额"}</option>
                            <option value="3" >${"营收同比增幅"}</option>
                            <option value="4" >${"扣非同比增幅"}</option>
                            <option value="5" >${"扣非营收百分比"}</option>
                            <option value="6" >${"更新时间"}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">名称</span>
                        <input type="text" class="form-control" id="company_name" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">公司股票代码</span>
                        <input type="text" class="form-control" id="stock_code" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info " id="exportBtn">导出</button>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info " id="searchBtn">查询</button>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" >
                            <table id="finance_detail_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
                                <tr>
                                    <th name="stockCompanyId" >id</th>
                                    <th name="name" >公司名称</th>
                                    <th name="lastPrice" >公司最新股价</th>
                                    <th name="totalIncome" >总营收(亿)</th>
                                    <th name="totalAddPercent" >营收同比增长(%)</th>
                                    <th name="netProfit" >扣非利润(亿)</th>
                                    <th name="netProfitPercent" >扣非同比增长(%)</th>
                                    <th name="profitTotalPercent" >扣非营收百分比(%)</th>
                                    <th name="reportTime" >发布时间</th>
                                    <th name="reportType" >报告类型</th>
                                    <th name="updatedTime" >更新时间</th>
                                    <th name="detail" >详情</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
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

<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/js/finance.js"></script>
</body>
</html>