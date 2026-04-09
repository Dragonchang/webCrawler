$(function() {
    var editor = CodeMirror.fromTextArea(document.getElementById('script_content'), {
        lineNumbers: true,
        mode: 'javascript',
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

    $('#saveBtn').on('click', function(){
        saveThen();
    });

    $('#publishBtn').on('click', function(){
        saveThen(function(){
            $.post(base_url + '/strategy/publish?id=' + $('#strategy_id').val(), function(resp){
                if (resp.code == 0) {
                    alert('发布成功');
                } else {
                    alert(resp.message || '发布失败');
                }
            });
        });
    });

    $('#runBtn').on('click', function(){
        saveThen(function(){
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

