// Copyright 2019 ALO7 Inc. All rights reserved.

package com.hf.util;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.TargetClassAware;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Jimi.feng
 * @mail Jimi.feng@alo7.com
 * @create 2019-12-16
 */
public class ProxyUtils {
    private static final int ALL_MODES = (MethodHandles.Lookup.PUBLIC
            | MethodHandles.Lookup.PRIVATE
            | MethodHandles.Lookup.PROTECTED
            | MethodHandles.Lookup.PACKAGE);

    private static Constructor<MethodHandles.Lookup> methodHandlerConstructor;

    static {
        try {
            methodHandlerConstructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class, int.class);
            methodHandlerConstructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Object safeDefaultMethodInvoke(Class clazz, Method method, Object proxy, Object[] args)
            throws Throwable {
        try {
            return methodHandlerConstructor.newInstance(clazz, ALL_MODES)
                    .unreflectSpecial(method, clazz)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> T createJavaProxy(Class<T> clazz, DefaultInvocationHandler handler) {
        return (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class[]{clazz},
                handler);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> T createJavaProxy(DefaultInvocationHandler handler, Class<?> clazz, Class<?>... others) {
        Class<?>[] allClass = ArrayUtils.add(others, clazz);
        return (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(), allClass, handler);
    }

    /**
     * Determine whether the object is a java proxy.
     */
    public static boolean isJavaProxy(Object obj) {
        return Proxy.isProxyClass(obj.getClass());
    }

    /**
     * Extract the target object from a cglib proxy.
     */
    public static Object extractInvocationHandlerFromJavaProxy(Object obj) {
        if (!isJavaProxy(obj)) {
            return null;
        }
        return Proxy.getInvocationHandler(obj);
    }

    /**
     * Get the target interface of a Java proxy.
     */
    public static Class<?> getTargetClassForJavaProxy(Object obj) {
        if (!isJavaProxy(obj)) {
            return null;
        }
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            return null;
        } else {
            return interfaces[0];
        }
    }

    /**
     * Extract the target object of a Java proxy. In order to use this method, the invocation handler must extend from
     * {@link DefaultInvocationHandler}.
     */
    public static Object extractTargetObjectForJavaProxy(@NotNull Object obj) {
        while (true) {
            if (obj == null || !isJavaProxy(obj)) {
                return obj;
            }

            InvocationHandler invocationHandler = Proxy.getInvocationHandler(obj);
            if (invocationHandler instanceof DefaultInvocationHandler) {
                obj = ((DefaultInvocationHandler) invocationHandler).getTargetObject();
            } else {
                // We do not know the implementation detail of the InvocationHandler,
                // so just return null.
                return null;
            }
        }
    }

    /**
     * Determine whether the object is a cglib proxy.
     */
    public static boolean isCglibProxy(Object obj) {
        return AopUtils.isCglibProxy(obj);
    }

    /**
     * Get the target interface of a Cglib proxy.
     */
    public static Class<?> getTargetClassForCglibProxy(Object obj) {
        if (isJavaProxy(obj)) {
            return null;
        }
        return AopProxyUtils.ultimateTargetClass(obj);
    }

    /**
     * Extract the target object from a cglib proxy.
     */
    @NotNull
    public static Object extractTargetObjectFromCglibProxy(@NotNull Object obj) {
        while (obj instanceof TargetClassAware) {
            Object target = AopProxyUtils.getSingletonTarget(obj);
            if (target == null) {
                return obj;
            } else {
                obj = target;
            }
        }
        return obj;
    }

    /**
     * Get the target interface of a Java proxy.
     */
    public static Class<?> getTargetClass(Object obj) {
        if (isJavaProxy(obj)) {
            return getTargetClassForJavaProxy(obj);
        } else if (isCglibProxy(obj)) {
            return getTargetClassForCglibProxy(obj);
        } else {
            return obj.getClass();
        }
    }

    /**
     * Safely invoke a method by removing InvocationTargetException.
     *
     * @param object The real object to invoke.
     * @param method The method of the object to invoke.
     * @param args The arguments for the method.
     * @return The method results.
     */
    public static Object safeMethodInvoke(Object object, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(object, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
