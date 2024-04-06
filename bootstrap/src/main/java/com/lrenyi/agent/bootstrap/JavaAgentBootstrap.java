package com.lrenyi.agent.bootstrap;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JavaAgentBootstrap {
    private static String AGENT_ROOT_DIR;
    
    public static void premain(String args, Instrumentation instrumentation) {
        try {
            javaAgentStart(instrumentation);
        } catch (Throwable cause) {
            printErrorInfo("javaAgentStart error: ", cause);
        }
    }
    
    private static void javaAgentStart(Instrumentation instrumentation) {
        String messagePrefix = "exception in obtaining the root directory of the Agent: ";
        URL location = JavaAgentBootstrap.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            AGENT_ROOT_DIR = location.toURI().getPath();
        } catch (URISyntaxException e) {
            printErrorInfo(messagePrefix, e);
            return;
        }
        String[] split = AGENT_ROOT_DIR.split("/");
        if (split.length == 0) {
            printErrorInfo(messagePrefix + AGENT_ROOT_DIR, null);
            return;
        }
        List<String> collect = Arrays.stream(split)
                                     .filter(JavaAgentBootstrap::haveLength)
                                     .collect(Collectors.toList());
        collect.remove(collect.size() - 1);
        if (collect.get(0).contains(":")) {
            AGENT_ROOT_DIR = String.join(File.separator, collect);
        } else {
            AGENT_ROOT_DIR = File.separator + String.join(File.separator, collect);
        }
        boolean success = addCoreJarFileToJvmBootstrap(instrumentation);
        if (!success) {
            return;
        }
        try (URLClassLoader classLoader = makeBootstrapClassLoader()) {
            Class<?> aClass = classLoader.loadClass("com.lrenyi.agent.core.AgentCoreStart");
            Method startAgent =
                    aClass.getDeclaredMethod("startAgent", String.class, Instrumentation.class);
            startAgent.invoke(null, AGENT_ROOT_DIR, instrumentation);
        } catch (Throwable cause) {
            printErrorInfo("invoke startAgent method fail: ", cause);
        }
    }
    
    private static URLClassLoader makeBootstrapClassLoader() throws MalformedURLException {
        ClassLoader classLoader = JavaAgentBootstrap.class.getClassLoader();
        String coreFile =
                AGENT_ROOT_DIR + File.separator + "core" + File.separator + "agent-core.jar";
        URL url = new File(coreFile).toURI().toURL();
        return new URLClassLoader(new URL[]{url}, classLoader);
    }
    
    private static boolean addCoreJarFileToJvmBootstrap(Instrumentation instrumentation) {
        String messagePrefix =
                "adding the core jar file to the bootstrap classloader search path failed: ";
        String coreFile =
                AGENT_ROOT_DIR + File.separator + "core" + File.separator + "bootstrap-core.jar";
        try {
            JarFile jarFile = new JarFile(coreFile);
            instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
        } catch (IOException e) {
            printErrorInfo(messagePrefix, e);
            return false;
        }
        return true;
    }
    
    public static void agentmain(String args, Instrumentation instrumentation) {
    
    }
    
    public static boolean haveLength(CharSequence context) {
        return context != null && !context.toString().trim().isEmpty();
    }
    
    private static void printErrorInfo(String message, Throwable cause) {
        System.err.println(
                "##########################################################################");
        System.err.println("JavaAgent start error, because: " + message);
        if (cause != null) {
            cause.printStackTrace(System.err);
        }
        System.err.println(
                "##########################################################################");
    }
}
