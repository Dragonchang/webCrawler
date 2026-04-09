<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <style>
        pre.log-panel { background: #111827; color: #d1fae5; border-radius: 4px; padding: 12px; min-height: 220px; white-space: pre-wrap; }
        pre.json-panel { background: #f7f7f7; border: 1px solid #ddd; border-radius: 4px; padding: 12px; min-height: 180px; white-space: pre-wrap; }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <@netCommon.commonLeft/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>策略运行详情</h1>
        </section>
        <section class="content">
            <input type="hidden" id="run_id" value="${runId?c}">
            <div class="row">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-header with-border"><h3 class="box-title">运行摘要</h3></div>
                        <div class="box-body">
                            <div class="row" id="summary_wrapper"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="box box-info">
                        <div class="box-header with-border"><h3 class="box-title">运行日志</h3></div>
                        <div class="box-body">
                            <pre class="log-panel" id="log_panel"></pre>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="box box-warning">
                        <div class="box-header with-border"><h3 class="box-title">结果预览</h3></div>
                        <div class="box-body">
                            <pre class="json-panel" id="result_panel"></pre>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="box">
                        <div class="box-header with-border"><h3 class="box-title">结果明细</h3></div>
                        <div class="box-body">
                            <table id="result_table" class="table table-bordered table-striped" width="100%">
                                <thead>
                                <tr>
                                    <th>排名</th>
                                    <th>股票代码</th>
                                    <th>股票名称</th>
                                    <th>动作</th>
                                    <th>评分</th>
                                    <th>原因</th>
                                    <th>因子明细</th>
                                </tr>
                                </thead>
                                <tbody id="result_tbody"></tbody>
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
<script src="${request.contextPath}/js/strategyRunDetail.js"></script>
</body>
</html>

