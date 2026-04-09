package com.dragonchang.strategy.script;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GroovyScriptCompiler {

    public Class<? extends Script> compile(String scriptContent) {
        CompilerConfiguration configuration = new CompilerConfiguration();
        SecureASTCustomizer secure = new SecureASTCustomizer();
        secure.setClosuresAllowed(true);
        secure.setMethodDefinitionAllowed(false);
        secure.setImportsBlacklist(Arrays.asList(
                "java.io.*",
                "java.nio.*",
                "java.net.*",
                "java.lang.reflect.*",
                "groovy.lang.GroovyShell"
        ));
        ImportCustomizer importCustomizer = new ImportCustomizer();
        configuration.addCompilationCustomizers(secure, importCustomizer);
        GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), configuration);
        return loader.parseClass(scriptContent).asSubclass(Script.class);
    }
}

