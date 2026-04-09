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
            <h1>策略管理</h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">策略名称</span>
                        <input type="text" class="form-control" id="strategy_name" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">策略编码</span>
                        <input type="text" class="form-control" id="strategy_code" autocomplete="on">
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">分类</span>
                        <select class="form-control" id="category">
                            <option value="">全部</option>
                            <option value="VALUE">价值</option>
                            <option value="GROWTH">成长</option>
                            <option value="TREND">趋势</option>
                            <option value="QUALITY">质量</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">状态</span>
                        <select class="form-control" id="status">
                            <option value="-1">全部</option>
                            <option value="0">草稿</option>
                            <option value="1">启用</option>
                            <option value="2">停用</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-success" id="createBtn">新建</button>
                </div>
                <div class="col-xs-1 pull-right">
                    <button class="btn btn-block btn-info" id="searchBtn">查询</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="strategy_list" class="table table-bordered table-striped" width="100%">
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>策略名称</th>
                                    <th>策略编码</th>
                                    <th>分类</th>
                                    <th>脚本类型</th>
                                    <th>当前版本</th>
                                    <th>状态</th>
                                    <th>调度方式</th>
                                    <th>最近运行状态</th>
                                    <th>最近运行时间</th>
                                    <th>更新时间</th>
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
<script src="${request.contextPath}/js/strategyList.js"></script>
</body>
</html>

