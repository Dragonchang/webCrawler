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
            <h1>公司信息: ${company.companyName}</h1>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">排序条件:</span>
                        <select class="form-control" id="condition" >
                            <option value="1" >${"投资金额"}</option>
                            <option value="2" >${"持股比例"}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="input-group">
                		<span class="input-group-addon">
	                  		创建时间
	                	</span>
                        <input type="text" class="form-control" id="filterTime" readonly >
                    </div>
                </div>
                <div class="col-xs-1">
                    <span id="company_id" style="display:none;">${company.id}</span>
                    <button class="btn btn-block btn-info" id="searchBtn">查询</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" >
                            <table id="shard_company_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
                                <tr>
                                    <th name="id" >id</th>
                                    <th name="companyId" >companyId</th>
                                    <th name="shareCompanyName" >持有股权公司名称</th>
                                    <th name="shareCompanyStockCode" >参股公司股票代码</th>
                                    <th name="shareCompanyAmount" >参股金额</th>
                                    <th name="shareCompanyType" >公司类型</th>
                                    <th name="shareCompanyBondType" >参股公司债券类型</th>
                                    <th name="shareCompanyFinanceLabel" >股权类型</th>
                                    <th name="shareCompanyPercent" >参股比例</th>
                                    <th name="shareCompanyBrand" >参股公司品牌</th>
                                    <th name="createdTime" >创建时间</th>
                                    <th name="stat" >统计</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list data as share>
                                    <tr>
                                        <td>${share.id}</td>
                                        <td>${share.companyId}</td>
                                        <td>${share.shareCompanyName!}</td>
                                        <td>${share.shareCompanyStockCode!}</td>
                                        <td>${share.shareCompanyAmount!}</td>
                                        <td>${share.shareCompanyType!}</td>
                                        <td>${share.shareCompanyBondType!}</td>
                                        <td>${share.shareCompanyFinanceLabel!}</td>
                                        <td>${share.shareCompanyPercent!}</td>
                                        <td>${share.shareCompanyBrand!}</td>
                                        <td>${share.createdTime!}</td>
                                        <td>
                                            <div class="col-xs-1">
                                                <button class="btn btn-warning btn-xs" id="searchBtn">统计</button>
                                            </div>
                                        </td>
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
<!-- daterangepicker -->
<script src="${request.contextPath}/js/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/js/share.js"></script>
</body>
</html>