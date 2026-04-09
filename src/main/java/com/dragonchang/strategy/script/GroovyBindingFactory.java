package com.dragonchang.strategy.script;

import com.dragonchang.strategy.api.LogApi;
import com.dragonchang.strategy.api.ResultApi;
import com.dragonchang.strategy.api.StrategyApi;
import com.dragonchang.strategy.engine.StrategyExecuteContext;
import groovy.lang.Binding;
import org.springframework.stereotype.Component;

@Component
public class GroovyBindingFactory {

    public Binding create(StrategyExecuteContext context,
                          StrategyApi strategyApi,
                          ResultApi resultApi,
                          LogApi logApi) {
        Binding binding = new Binding();
        binding.setVariable("api", strategyApi);
        binding.setVariable("params", context.getParams());
        binding.setVariable("context", context);
        binding.setVariable("result", resultApi);
        binding.setVariable("log", logApi);
        return binding;
    }
}

