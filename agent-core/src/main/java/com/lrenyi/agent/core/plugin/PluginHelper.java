package com.lrenyi.agent.core.plugin;

import com.lrenyi.agent.core.matcher.AbstractJunction;
import com.lrenyi.agent.core.matcher.ProtectiveShieldMatcher;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public class PluginHelper {
    
    public static ElementMatcher<? super TypeDescription> basicMatch() {
        AbstractJunction<NamedElement> abstractJunction = new AbstractJunction<NamedElement>() {
            @Override
            public boolean matches(NamedElement target) {
                return true;
            }
        };
        return new ProtectiveShieldMatcher<>(abstractJunction);
    }
}
