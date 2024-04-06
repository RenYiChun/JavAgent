package com.lrenyi.agent.core.listener;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.bytebuddy.agent.builder.AgentBuilder;

public class RedefinitionListener implements AgentBuilder.RedefinitionStrategy.Listener {
    @Override
    public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
        //ignore
    }
    
    @Override
    public Iterable<? extends List<Class<?>>> onError(int index,
            List<Class<?>> batch,
            Throwable throwable,
            List<Class<?>> types) {
        return Collections.emptyList();
    }
    
    @Override
    public void onComplete(int amount,
            List<Class<?>> types,
            Map<List<Class<?>>, Throwable> failures) {
        //ignore
    }
}
