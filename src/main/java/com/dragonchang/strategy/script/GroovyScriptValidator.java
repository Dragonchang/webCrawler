package com.dragonchang.strategy.script;

import com.dragonchang.domain.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroovyScriptValidator {

    @Autowired
    private GroovyScriptCompiler groovyScriptCompiler;

    public JsonResult validate(String scriptContent) {
        if (scriptContent == null || scriptContent.trim().isEmpty()) {
            return JsonResult.failure("Groovy脚本不能为空");
        }
        try {
            groovyScriptCompiler.compile(scriptContent);
            return JsonResult.success();
        } catch (Exception e) {
            return JsonResult.failure("脚本校验失败: " + e.getMessage());
        }
    }
}

