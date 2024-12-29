package com.example.mossymobile.MossFramework.DesignPatterns;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Singleton<T extends Singleton<T>> {
    protected Singleton() {}

    private static final ConcurrentHashMap<Class<?>, Singleton<?>> instances = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Singleton<T>> T GetInstance(Class<T> singletonClass) {
        // Retrieve the instance from the map, or create it if absent
        return (T) instances.computeIfAbsent(singletonClass, key -> {
            try {
                return singletonClass.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Failed to create instance for: " + singletonClass.getName(), e);
            }
        });
    }
}
