package com.hsdyz111.xptool.compat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionHelper {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Class not found: {}", className);
            return null;
        }
    }
    
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null) return null;
        
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            LOGGER.warn("Method not found: {} in class {}", methodName, clazz.getName());
            return null;
        }
    }
    
    public static Field getField(Class<?> clazz, String fieldName) {
        if (clazz == null) return null;
        
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            LOGGER.warn("Field not found: {} in class {}", fieldName, clazz.getName());
            return null;
        }
    }
    
    public static Object invokeMethod(Method method, Object instance, Object... args) {
        if (method == null) return null;
        
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            LOGGER.warn("Failed to invoke method: {}", method.getName(), e);
            return null;
        }
    }
    
    public static Object getFieldValue(Field field, Object instance) {
        if (field == null) return null;
        
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            LOGGER.warn("Failed to access field: {}", field.getName(), e);
            return null;
        }
    }
}
