package com.lrenyi.agent.core;

import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

import com.lrenyi.agent.core.listener.RedefinitionListener;
import com.lrenyi.agent.core.listener.TransformerListener;
import com.lrenyi.agent.core.plugin.PluginHelper;
import com.lrenyi.agent.core.transformer.ClassTransformer;
import java.lang.instrument.Instrumentation;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.scaffold.TypeValidation;

public class AgentCoreStart {
    
    public static void startAgent(String root, Instrumentation instrumentation) {
        ByteBuddy byteBuddy = new ByteBuddy().ignore(nameStartsWith("com.lrenyi.agent"))
                                             .with(TypeValidation.ENABLED);
        AgentBuilder.Default agentBuilder = new AgentBuilder.Default();
        agentBuilder.with(byteBuddy)
                    .type(PluginHelper.basicMatch())
                    .transform(new ClassTransformer())
                    .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                    .with(new RedefinitionListener())
                    .with(new TransformerListener())
                    .installOn(instrumentation);
    }
}
