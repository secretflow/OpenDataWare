package com.cec.examine.util;

import com.cec.examine.desensitization.SensitivePatternRegex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ImplementationFinder {

    public static List<Class<?>> findImplementationsOfAbstractClass(String packageName, Class<?> abstractClass) {
        List<Class<?>> implementations = new ArrayList<>();
        String path = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File file = new File(resource.getFile());
                findClassesInPackage(packageName, file, implementations, abstractClass);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return implementations;
    }

    private static void findClassesInPackage(String packageName, File directory, List<Class<?>> implementations, Class<?> abstractClass) {
        if (!directory.exists()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                findClassesInPackage(packageName + "." + file.getName(), file, implementations, abstractClass);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (!Modifier.isAbstract(clazz.getModifiers()) && abstractClass.isAssignableFrom(clazz)) {
                        implementations.add(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String packageName = "com.cec.examine.desensitization"; // Replace with your package name
        Class<?> abstractClass = SensitivePatternRegex.class; // Replace with your abstract class name

        List<Class<?>> implementations = findImplementationsOfAbstractClass(packageName, abstractClass);

        System.out.println("Implementations of " + abstractClass.getName() + ":");
        for (Class<?> implementation : implementations) {
            System.out.println(implementation.getName());
        }
    }
}
