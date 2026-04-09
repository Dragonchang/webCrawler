<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <@netCommon.commonLeft/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>策略运行记录</h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">策略名称</span>
                        <input type="text" class="form-control" id="strategy_name" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">运行状态</span>
                        <select class="form-control" id="run_status">
                            <option value="">全部</option>
                            <option value="SUCCESS">成功</option>
                            <option value="FAIL">失败</option>
                            <option value="RUNNING">运行中</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info" id="searchBtn">查询</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="strategy_run_list" class="table table-bordered table-striped" width="100%">
                                <thead>
                                <tr>
                                    <th>运行ID</th>
                                    <th>策略名称</th>
                                    <th>策略编码</th>
                                    <th>版本号</th>
                                    <th>执行器</th>
                                    <th>脚本类型</th>
                                    <th>运行方式</th>
                                    <th>状态</th>
                                    <th>开始时间</th>
                                    <th>结束时间</th>
                                    <th>耗时(ms)</th>
                                    <th>结果数量</th>
                                    <th>错误摘要</th>
                                    <th>操作</th>
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
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/js/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/js/strategyRunList.js"></script>
</body>
</html>

