<!DOCTYPE html>
<html>
<head>
    <title>drip</title>
    <#import "./common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/js/plugins/codemirror/lib/codemirror.css">
    <link rel="stylesheet" href="${request.contextPath}/js/plugins/codemirror/addon/hint/show-hint.css">
    <style>
        .CodeMirror { border: 1px solid #d2d6de; height: 420px; }
        .help-block { color: #777; }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <@netCommon.commonLeft/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>策略编辑</h1>
        </section>
        <section class="content">
            <div class="row">
                <div class="col-md-4">
                    <div class="box box-primary">
                        <div class="box-header with-border"><h3 class="box-title">基础信息</h3></div>
                        <div class="box-body">
                            <input type="hidden" id="strategy_id" value="<#if strategy??>${strategy.id?c}</#if>">
                            <div class="form-group">
                                <label>策略名称</label>
                                <input type="text" class="form-control" id="strategy_name" value="<#if strategy??>${strategy.strategyName!}</#if>">
                            </div>
                            <div class="form-group">
                                <label>策略编码</label>
                                <input type="text" class="form-control" id="strategy_code" value="<#if strategy??>${strategy.strategyCode!}</#if>">
                            </div>
                            <div class="form-group">
                                <label>分类</label>
                                <select class="form-control" id="category">
                                    <option value="VALUE" <#if strategy?? && strategy.category?? && strategy.category == 'VALUE'>selected</#if>>价值</option>
                                    <option value="GROWTH" <#if strategy?? && strategy.category?? && strategy.category == 'GROWTH'>selected</#if>>成长</option>
                                    <option value="TREND" <#if strategy?? && strategy.category?? && strategy.category == 'TREND'>selected</#if>>趋势</option>
                                    <option value="QUALITY" <#if strategy?? && strategy.category?? && strategy.category == 'QUALITY'>selected</#if>>质量</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>脚本类型</label>
                                <select class="form-control" id="script_type">
                                    <option value="RULE" <#if strategy?? && strategy.scriptType?? && strategy.scriptType == 'RULE'>selected</#if>>规则脚本</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>调度类型</label>
                                <select class="form-control" id="schedule_type">
                                    <option value="MANUAL" <#if strategy?? && strategy.scheduleType?? && strategy.scheduleType == 'MANUAL'>selected</#if>>手动</option>
                                    <option value="CRON" <#if strategy?? && strategy.scheduleType?? && strategy.scheduleType == 'CRON'>selected</#if>>定时</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Cron 表达式</label>
                                <input type="text" class="form-control" id="cron_expr" value="<#if strategy??>${strategy.cronExpr!}</#if>" placeholder="0 30 15 ? * MON-FRI">
                            </div>
                            <div class="form-group">
                                <label>策略描述</label>
                                <textarea class="form-control" rows="4" id="description"><#if strategy??>${strategy.description!}</#if></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="box box-info">
                        <div class="box-header with-border"><h3 class="box-title">参数配置</h3></div>
                        <div class="box-body">
                            <div class="form-group">
                <label>默认参数 JSON</label>
                                <textarea class="form-control" rows="8" id="default_params"><#if strategy??>${(strategy.defaultParams!'')?html}</#if></textarea>
                                <p class="help-block">示例：<code>{"minMarketCap":100,"maxPe":30,"minPrice":5,"minIncome":1000000000,"limit":20}</code></p>
                            </div>
                            <div class="form-group">
                                <label>参数 Schema JSON</label>
                                <textarea class="form-control" rows="6" id="param_schema"><#if strategy??>${(strategy.paramSchema!'')?html}</#if></textarea>
                            </div>
                            <div class="form-group">
                                <label>股票池配置 JSON</label>
                                <textarea class="form-control" rows="6" id="universe_config"><#if strategy??>${(strategy.universeConfig!'')?html}</#if></textarea>
                                <p class="help-block">留空表示全市场；示例：<code>{"type":"BK","bkName":"人工智能"}</code></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-8">
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">规则脚本</h3>
                            <div class="box-tools pull-right">
                                <button class="btn btn-info btn-sm" id="saveBtn">保存草稿</button>
                                <button class="btn btn-warning btn-sm" id="publishBtn">发布版本</button>
                                <button class="btn btn-success btn-sm" id="runBtn">运行测试</button>
                            </div>
                        </div>
                        <div class="box-body">
                            <textarea id="script_content"><#if strategy??>${(strategy.scriptContent!'')?html}</#if></textarea>
                            <p class="help-block">当前第一阶段支持规则型脚本，作为策略说明文本保留；真正运行逻辑由默认参数控制。</p>
                        </div>
                    </div>
                    <div class="box box-default">
                        <div class="box-header with-border"><h3 class="box-title">运行说明</h3></div>
                        <div class="box-body">
                            <ul>
                                <li>第一阶段支持按默认参数执行选股筛选。</li>
                                <li>保存后可发布版本，发布后可运行。</li>
                                <li>运行完成后可跳转到运行详情页面查看日志与结果。</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
    <@netCommon.commonFooter />
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/js/plugins/codemirror/lib/codemirror.js"></script>
<script src="${request.contextPath}/js/plugins/codemirror/mode/javascript/javascript.js"></script>
<script src="${request.contextPath}/js/strategyEdit.js"></script>
</body>
</html>

