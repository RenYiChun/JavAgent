package com.lrenyi.agent.bootstrap.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class BootstrapClassLoader extends URLClassLoader {
    
    public BootstrapClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
