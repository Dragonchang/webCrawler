<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <!-- left -->
    <@netCommon.commonLeft/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>公司管理</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">公司名称</span>
                        <input type="text" class="form-control" id="company_name" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">天眼查ID</span>
                        <input type="text" class="form-control" id="tyc_id" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">公司股票代码</span>
                        <input type="text" class="form-control" id="stock_code" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-1">
                    <button class="btn btn-block btn-info" id="searchBtn">查询</button>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-success add" type="button">添加公司</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body" >
                            <table id="company_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
                                <tr>
                                    <th name="id" >id</th>
                                    <th name="tycId" >天眼查ID</th>
                                    <th name="companyName" >公司名称</th>
                                    <th name="stockCode" >股票代码</th>
                                    <th name="createdTime" >创建时间</th>
                                    <th name="detail" >详情</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                                <tfoot></tfoot>
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
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/js/company.js"></script>
</body>
</html>