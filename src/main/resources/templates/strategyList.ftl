<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <style>
        .live-run-card { margin-bottom: 15px; }
        .strategy-live-board { display:flex; gap:20px; align-items:center; flex-wrap:wrap; }
        .strategy-live-stat { min-width: 160px; }
        .strategy-live-stat .label-text { color:#999; font-size:12px; display:block; }
        .strategy-live-stat .value-text { color:#222; font-size:18px; font-weight:600; }
        .mini-progress-wrap { min-width: 220px; }
        .mini-progress-wrap .progress { margin-bottom:4px; }
        .run-status-pill { display:inline-block; padding:2px 8px; border-radius:10px; font-size:12px; color:#fff; }
        .run-status-pill.idle { background:#9aa5b1; }
        .run-status-pill.running { background:#00a65a; }
        .run-status-pill.success { background:#3c8dbc; }
        .run-status-pill.fail { background:#dd4b39; }
        .run-status-text { line-height:1.5; }
        .run-status-sub { color:#999; font-size:12px; margin-top:3px; }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <@netCommon.commonLeft/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>策略管理</h1>
        </section>
        <section class="content">
            <div class="row live-run-card">
                <div class="col-xs-12">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">实时运行看板</h3>
                        </div>
                        <div class="box-body strategy-live-board">
                            <div class="strategy-live-stat">
                                <span class="label-text">运行中的策略</span>
                                <span class="value-text" id="live_running_count">0</span>
                            </div>
                            <div class="strategy-live-stat">
                                <span class="label-text">最近实时消息</span>
                                <span class="value-text" id="live_last_event">等待连接</span>
                            </div>
                            <div class="mini-progress-wrap">
                                <div class="label-text">最近一条进度</div>
                                <div class="progress progress-sm active">
                                    <div id="live_global_progress_bar" class="progress-bar progress-bar-success" style="width:0%"></div>
                                </div>
                                <div class="run-status-sub" id="live_global_progress_text">0%</div>
                            </div>
                            <div class="strategy-live-stat" style="min-width:320px;">
                                <span class="label-text">消息摘要</span>
                                <span class="run-status-text" id="live_global_message">尚未收到策略运行推送</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
                                    <th>校验状态</th>
                                    <th>当前版本</th>
                                    <th>状态</th>
                                    <th>调度方式</th>
                                    <th>最近运行状态</th>
                                    <th>实时运行状态</th>
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
