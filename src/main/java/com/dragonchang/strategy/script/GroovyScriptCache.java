package com.dragonchang.strategy.script;

import groovy.lang.Script;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroovyScriptCache {
    private final Map<String, Class<? extends Script>> cache = new ConcurrentHashMap<String, Class<? extends Script>>();

    public Class<? extends Script> get(String scriptHash) {
        return cache.get(scriptHash);
    }

    public void put(String scriptHash, Class<? extends Script> scriptClass) {
        cache.put(scriptHash, scriptClass);
    }
}

