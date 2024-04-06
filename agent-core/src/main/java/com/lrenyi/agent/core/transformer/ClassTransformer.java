package com.lrenyi.agent.core.transformer;

import static net.bytebuddy.jar.asm.Opcodes.ACC_PRIVATE;
import static net.bytebuddy.jar.asm.Opcodes.ACC_VOLATILE;

import com.lrenyi.agent.bootstrap.core.CacheFieldInstance;
import com.lrenyi.agent.core.utils.AgentConstant;
import java.security.ProtectionDomain;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.utility.JavaModule;

public class ClassTransformer implements AgentBuilder.Transformer {
    
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain) {
        
        String typeName = typeDescription.getTypeName();
        if ("com.mysql.cj.jdbc.ConnectionImpl".equals(typeName)) {
            int modifiers = ACC_PRIVATE | ACC_VOLATILE;
            builder = builder.defineField(AgentConstant.CACHE_FIELD_NAME, Object.class, modifiers)
                             .implement(CacheFieldInstance.class)
                             .intercept(FieldAccessor.ofField(AgentConstant.CACHE_FIELD_NAME));
        }
        return builder;
    }
}
