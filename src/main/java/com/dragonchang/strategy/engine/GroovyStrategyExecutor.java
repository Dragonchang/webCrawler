package com.dragonchang.strategy.engine;

import com.dragonchang.strategy.api.LogApi;
import com.dragonchang.strategy.api.ResultApi;
import com.dragonchang.strategy.api.StrategyApi;
import com.dragonchang.strategy.script.GroovyBindingFactory;
import com.dragonchang.strategy.script.GroovyScriptCache;
import com.dragonchang.strategy.script.GroovyScriptCompiler;
import com.dragonchang.strategy.support.StrategyFinanceDataService;
import com.dragonchang.strategy.support.StrategyMarketDataService;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class GroovyStrategyExecutor implements StrategyExecutor {

    @Autowired
    private GroovyScriptCompiler groovyScriptCompiler;
    @Autowired
    private GroovyScriptCache groovyScriptCache;
    @Autowired
    private GroovyBindingFactory groovyBindingFactory;
    @Autowired
    private StrategyFinanceDataService strategyFinanceDataService;
    @Autowired
    private StrategyMarketDataService strategyMarketDataService;

    @Override
    public String getScriptType() {
        return "GROOVY";
    }

    @Override
    public StrategyExecuteResult execute(StrategyExecuteContext context) {
        StrategyApi strategyApi = new StrategyApi(context.getUniverse(), strategyFinanceDataService,
                strategyMarketDataService, context.getFinanceMap());
        ResultApi resultApi = new ResultApi(context.getRunId());
        LogApi logApi = new LogApi(context.getRunId(), context.getStrategyId(), context.getPushService());
        logApi.info("开始执行Groovy策略：" + context.getStrategyInfo().getStrategyName());
        logApi.info("股票池数量=" + (context.getUniverse() == null ? 0 : context.getUniverse().size()));
        if (context.getPushService() != null) {
            context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 65, "GROOVY_PREPARE", "Groovy脚本准备执行");
        }

        String scriptHash = context.getStrategyVersion().getScriptHash();
        Class<? extends Script> scriptClass = groovyScriptCache.get(scriptHash);
        if (scriptClass == null) {
            scriptClass = groovyScriptCompiler.compile(context.getStrategyVersion().getScriptContent());
            groovyScriptCache.put(scriptHash, scriptClass);
            logApi.info("Groovy脚本编译完成并写入缓存");
            if (context.getPushService() != null) {
                context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 72, "GROOVY_COMPILE", "Groovy脚本编译完成");
            }
        } else {
            logApi.info("命中Groovy脚本缓存");
            if (context.getPushService() != null) {
                context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 72, "GROOVY_CACHE", "命中Groovy脚本缓存");
            }
        }

        final Class<? extends Script> finalScriptClass = scriptClass;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            Future<?> future = executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Script script = finalScriptClass.newInstance();
                    Binding binding = groovyBindingFactory.create(context, strategyApi, resultApi, logApi);
                    script.setBinding(binding);
                    if (context.getPushService() != null) {
                        context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 78, "GROOVY_RUNNING", "Groovy脚本运行中");
                    }
                    return script.run();
                }
            });
            future.get();
            logApi.info("Groovy策略执行完成，结果数量=" + resultApi.getResults().size());
            if (context.getPushService() != null) {
                context.getPushService().pushProgress(context.getRunId(), context.getStrategyId(), "RUNNING", 92, "GROOVY_RESULT_READY", "Groovy脚本执行完成，结果待持久化");
            }
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() == null ? e : e.getCause();
            String message = cause.getMessage() == null ? cause.toString() : cause.getMessage();
            logApi.error("Groovy策略执行异常：" + message);
            throw new RuntimeException(message, cause);
        } catch (Exception e) {
            String message = e.getMessage() == null ? e.toString() : e.getMessage();
            logApi.error("Groovy策略执行异常：" + message);
            throw new RuntimeException(message, e);
        } finally {
            executorService.shutdownNow();
        }

        StrategyExecuteResult result = new StrategyExecuteResult();
        result.setResults(resultApi.getResults());
        result.setLogs(logApi.getLogs());
        result.setResultCount(resultApi.getResults().size());
        result.setProgress(95);
        result.setStatus("SUCCESS");
        result.setSummary("Groovy策略执行完成");
        return result;
    }
}
