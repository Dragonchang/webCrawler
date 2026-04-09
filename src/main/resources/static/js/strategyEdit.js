$(function() {
    var editor = CodeMirror.fromTextArea(document.getElementById('script_content'), {
        lineNumbers: true,
        mode: 'text/x-java',
        lineWrapping: true
    });

    function getPayload() {
        return {
            id: $('#strategy_id').val() ? parseInt($('#strategy_id').val()) : null,
            strategyName: $('#strategy_name').val(),
            strategyCode: $('#strategy_code').val(),
            category: $('#category').val(),
            description: $('#description').val(),
            scriptType: $('#script_type').val(),
            scriptContent: editor.getValue(),
            paramSchema: $('#param_schema').val(),
            defaultParams: $('#default_params').val(),
            universeConfig: $('#universe_config').val(),
            scheduleType: $('#schedule_type').val(),
            cronExpr: $('#cron_expr').val()
        };
    }

    function refreshScriptTip() {
        if ($('#script_type').val() === 'GROOVY') {
            $('#script_tip').html('当前为 <b>Groovy脚本</b> 模式：脚本会在后台执行器中真正运行，可使用 <code>api</code>、<code>params</code>、<code>result</code>、<code>log</code>。');
        } else {
            $('#script_tip').html('当前为 <b>规则脚本</b> 模式：脚本文本主要作为说明保留，执行逻辑仍由默认参数驱动。');
        }
    }

    function saveThen(callback) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: base_url + '/strategy/save',
            data: JSON.stringify(getPayload()),
            success: function(resp) {
                if (resp.code == 0) {
                    if (!$('#strategy_id').val() && resp.data) {
                        $('#strategy_id').val(resp.data);
                    }
                    if (callback) {
                        callback();
                    } else {
                        alert('保存成功');
                    }
                } else {
                    alert(resp.message || '保存失败');
                }
            }
        });
    }

    function validateScript(callback) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: base_url + '/strategy/validate',
            data: JSON.stringify({
                id: $('#strategy_id').val() ? parseInt($('#strategy_id').val()) : null,
                scriptType: $('#script_type').val(),
                scriptContent: editor.getValue()
            }),
            success: function(resp) {
                $('#validate_status').val(resp.code == 0 ? 'SUCCESS' : 'FAIL');
                $('#validate_message').val(resp.code == 0 ? '校验成功' : (resp.message || '校验失败'));
                if (callback) {
                    callback(resp);
                } else {
                    alert(resp.code == 0 ? '校验通过' : (resp.message || '校验失败'));
                }
            }
        });
    }

    $('#saveBtn').on('click', function(){
        saveThen();
    });

    $('#validateBtn').on('click', function(){
        saveThen(function() {
            validateScript();
        });
    });

    $('#templateBtn').on('click', function(){
        $.get(base_url + '/strategy/groovyTemplates', function(resp){
            if (resp.code == 0 && resp.data && resp.data.length > 0) {
                editor.setValue(resp.data[0].content || '');
                $('#script_type').val('GROOVY');
                refreshScriptTip();
            } else {
                alert('未获取到模板');
            }
        });
    });

    $('#publishBtn').on('click', function(){
        saveThen(function(){
            validateScript(function(validateResp) {
                if ($('#script_type').val() === 'GROOVY' && validateResp.code != 0) {
                    return;
                }
                $.post(base_url + '/strategy/publish?id=' + $('#strategy_id').val(), function(resp){
                    if (resp.code == 0) {
                        alert('发布成功');
                    } else {
                        alert(resp.message || '发布失败');
                    }
                });
            });
        });
    });

    $('#runBtn').on('click', function(){
        saveThen(function(){
            validateScript(function(validateResp) {
                if ($('#script_type').val() === 'GROOVY' && validateResp.code != 0) {
                    return;
                }
                $.post(base_url + '/strategy/publish?id=' + $('#strategy_id').val(), function(resp){
                    if (resp.code == 0) {
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            contentType: 'application/json',
                            url: base_url + '/strategyRun/execute',
                            data: JSON.stringify({strategyId: parseInt($('#strategy_id').val()), params: $('#default_params').val()}),
                            success: function(runResp){
                                if (runResp.code == 0) {
                                    alert('任务已提交，正在后台执行');
                                    window.open(base_url + '/strategyRun/detailPage?runId=' + runResp.data.runId, '_self');
                                } else {
                                    alert(runResp.message || '运行失败');
                                }
                            }
                        });
                    } else {
                        alert(resp.message || '发布失败');
                    }
                });
            });
        });
    });

    $('#script_type').on('change', function(){
        refreshScriptTip();
    });

    refreshScriptTip();
});
