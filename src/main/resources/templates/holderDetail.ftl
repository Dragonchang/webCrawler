<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- left -->
    <@netCommon.commonLeft />
    <div class="content-wrapper">
        <section class="content-header">
            <h1>持股名称: ${name}</h1>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info" id="addBtn">加关注</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" >
                            <table id="shard_company_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
                                <tr>
                                    <th width="70" name="name" >公司名称</th>
                                    <th name="stockCode" >公司股票代码</th>
                                    <th name="lastPrice" >公司最新股价</th>
                                    <th name="lastCirculation" >公司最新流通市值</th>
                                    <th name="totalCapitalization" >公司最新总市值</th>
                                    <th name="lastIncome" >公司最新收益</th>
                                    <th name="holderRank" >股东排名</th>
                                    <th name="holderName" >持股人/机构名称</th>
                                    <th name="holdCount" >持股数(股)</th>
                                    <th name="holdPercent" >占总流通股本持股比例</th>
                                    <th name="zj" >增减</th>
                                    <th name="changePercent" >变动比例</th>
                                    <th name="holderType" >股东类型</th>
                                    <th name="reportTime" >股东信息发布时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list holderList as holder>
                                    <tr>
                                        <td>${holder.name}</td>
                                        <td>${holder.stockCode}</td>
                                        <td>${holder.lastPrice!}</td>
                                        <td>${holder.lastCirculation!}</td>
                                        <td>${holder.totalCapitalization!}</td>
                                        <td>${holder.lastIncome!}</td>
                                        <td>${holder.holderRank!}</td>
                                        <td>${holder.holderName!}</td>
                                        <td>${holder.holdCount!}</td>
                                        <td>${holder.holdPercent!}</td>
                                        <td>${holder.zj!}</td>
                                        <td>${holder.changePercent!}</td>
                                        <td>${holder.holderType!}</td>
                                        <td>${holder.reportTime!}</td>
                                    </tr>
                                </#list>
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
</body>
</html>