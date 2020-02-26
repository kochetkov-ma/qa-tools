package ru.iopump.qa.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
@SuppressWarnings("unused")
public class ReflectionUtil {

    @NonNull
    public static Collection<Class<?>> getGenericTypes(@NonNull Field field) {
        final Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return null;
        return StreamUtil.stream(((ParameterizedType) genericType).getActualTypeArguments())
                .map(t -> (Class<?>) t)
                .collect(Collectors.toList());
    }

    @NonNull
    public static <T> Collection<T> createImplementations(@NonNull Class<T> interfaceOrSuperClass, @Nullable String packageName) {
        //noinspection unchecked
        return StreamUtil.stream(findImplementations(interfaceOrSuperClass, packageName))
                .map(aClass -> (T) Reflect.onClass(aClass).create().get())
                .collect(Collectors.toList());
    }

    @NonNull
    public static <T> Collection<Class<T>> findImplementations(@NonNull Class<T> interfaceOrSuperClass, @Nullable String packageName) {
        if (StringUtils.isBlank(packageName)) {
            return Collections.emptyList();
        }
        try (final ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(packageName).scan()) {
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
    public static Optional<Class<?>> findOneClassBySimpleNameInJdk(@Nullable String classSimpleName) {
        if (StringUtils.isBlank(classSimpleName)) {
            return Optional.empty();
        }
        try (final ScanResult scanResult = new ClassGraph()
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