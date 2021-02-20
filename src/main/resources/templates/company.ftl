<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="${request.contextPath}/plugins/fontawesome-free/css/all.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="${request.contextPath}/dist/css/adminlte.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
    <!-- left -->
    <@netCommon.commonLeft/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>公司管理</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-auto">
                        <div class="input-group">
                            <span class="input-group-addon text-center">公司名称</span>
                            <input type="text" class="form-control" id="company_name" autocomplete="on" >
                        </div>
                    </div>
                    <div class="col-sm-auto">
                        <div class="input-group">
                            <span class="input-group-addon text-center">天眼查ID</span>
                            <input type="text" class="form-control" id="tyc_id" autocomplete="on" >
                        </div>
                    </div>
                    <div class="col-sm-auto">
                        <div class="input-group">
                            <span class="input-group-addon text-center">公司股票代码</span>
                            <input type="text" class="form-control" id="stock_code" autocomplete="on" >
                        </div>
                    </div>
                    <div class="col-sm-auto">
                        <button class="btn btn-block btn-info" id="searchBtn">查询</button>
                    </div>
                    <div class="col-sm-auto">
                        <button class="btn btn-block btn-success" type="button">添加公司</button>
                    </div>
                </div>
            </div>
        </section>

        <section class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xl-8">
                        <div class="box">
                            <div class="box-body" >
                                <table id="company_list" class="table table-bordered text-center" width="100%" >
                                    <thead>
                                    <tr>
                                        <th name="tycId" >天眼查ID</th>
                                        <th name="companyName" >公司名称</th>
                                        <th name="stockCode" >股票代码</th>
                                        <th name="createdTime" >创建时间</th>
                                        <th>详情</th>
                                    </tr>
                                    </thead>
                                    <tbody></tbody>
                                    <tfoot></tfoot>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/js/company.js"></script>
</body>
</html>