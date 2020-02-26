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

    /**
     * Get collection of parameters types of passed {@link Field}.
     * You can get generic types only from {@link Field} (or {@link Class}).
     * For object this information is been removed.
     * Null-safe.
     *
     * @param field field with parameters type
     * @return collection of generic types
     */
    @NonNull
    public static Collection<Class<?>> getGenericTypes(@Nullable Field field) {
        if (field == null) return Collections.emptyList();
        final Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return null;
        return StreamUtil.stream(((ParameterizedType) genericType).getActualTypeArguments())
                .map(t -> (Class<?>) t)
                .collect(Collectors.toList());
    }

    /**
     * Find all implementations of passed type and create it via non-arg constructor.
     * Null-safe.
     *
     * @param interfaceOrSuperClass Base type
     * @param packageName           full package name to search
     * @param <T>                   Expected type
     * @return Collection of children.
     */
    @NonNull
    public static <T> Collection<T> createImplementations(@Nullable Class<T> interfaceOrSuperClass,
                                                          @Nullable String packageName) {
        //noinspection unchecked
        return StreamUtil.stream(findImplementations(interfaceOrSuperClass, packageName))
                .map(aClass -> (T) Reflect.onClass(aClass).create().get())
                .collect(Collectors.toList());
    }

    /**
     * Find all implementations of passed type.
     * Null-safe.
     *
     * @param interfaceOrSuperClass Base type
     * @param packageName           full package name to search
     * @param <T>                   Expected type
     * @return Collection of children.
     */
    @NonNull
    public static <T> Collection<Class<T>> findImplementations(@Nullable Class<T> interfaceOrSuperClass,
                                                               @Nullable String packageName) {
        if (StringUtils.isBlank(packageName) || interfaceOrSuperClass == null) return Collections.emptyList();
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

    /**
     * Find class in JDK packages by his simple name.
     * Search will be only in 'java.lang' and 'java.util' because.
     * For example 'String' will find {@link String} class.
     * Null-safe.
     *
     * @param classSimpleName Simple class name (not full) in JDK.
     * @return Related class
     */
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