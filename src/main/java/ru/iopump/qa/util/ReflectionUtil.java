package ru.iopump.qa.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.NonNull;
import org.joor.Reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectionUtil {
    @NonNull
    public Class<?> getCollectionGenericType(@NonNull Field field) {
        final Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return null;
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    @NonNull
    public <T> Collection<T> createImplementations(@NonNull Class<T> interfaceOrSuperClass, @NonNull String packageName) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(packageName).scan()) {
            final ClassInfoList controlClasses = scanResult.getClassesImplementing(interfaceOrSuperClass.getName());
            final List<Class<T>> controlClassRefs = controlClasses
                    .filter(classInfo -> !classInfo.isAbstract())
                    .loadClasses(interfaceOrSuperClass);
            //noinspection unchecked
            return StreamUtil.stream(controlClassRefs)
                    .map(aClass -> (T) Reflect.onClass(aClass).create().get())
                    .collect(Collectors.toList());
        }
    }

    @NonNull
    public <T> Collection<Class<T>> findImplementations(@NonNull Class<T> interfaceOrSuperClass, @NonNull String packageName) {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(packageName).scan()) {
            final ClassInfoList implControlClasses;
            if (interfaceOrSuperClass.isInterface()) {
                implControlClasses = scanResult.getClassesImplementing(interfaceOrSuperClass.getName());
            } else {
                implControlClasses = new ClassInfoList();
            }
            final ClassInfoList controlClasses = scanResult.getSubclasses(interfaceOrSuperClass.getName());
            controlClasses.addAll(implControlClasses);
            return controlClasses
                    .filter(classInfo -> !classInfo.isAbstract())
                    .loadClasses(interfaceOrSuperClass);
        }
    }

    @NonNull
    public Optional<Class<?>> findOneClassBySimpleNameInJdk(@NonNull String classSimpleName) {
        try (ScanResult scanResult = new ClassGraph()
                .addClassLoader(ClassLoader.getSystemClassLoader())
                .enableSystemJarsAndModules()
                .whitelistPackages("java.lang", "java.util")
                .scan()) {
            final ClassInfoList controlClasses = scanResult.getAllClasses();
            return controlClasses
                    .filter(classInfo -> classSimpleName.equalsIgnoreCase(classInfo.getSimpleName()))
                    .directOnly()
                    .stream()
                    .findFirst()
                    .map(ClassInfo::loadClass);
        }
    }
}