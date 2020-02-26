package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class StreamUtil {

    @SafeVarargs
    @NonNull
    public static <T> Stream<T> stream(@Nullable T... array) {
        if (array == null) return Stream.empty();
        return stream(Arrays.asList(array));
    }

    @NonNull
    public static <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
        return StreamSupport.stream(Optional.ofNullable(iterable)
                .map(Iterable::spliterator).orElse(Spliterators.emptySpliterator()), false);
    }

    @NonNull
    public static <K, V> Stream<Map.Entry<K, V>> stream(@Nullable Map<K, V> map) {
        return stream(Optional.ofNullable(map).map(Map::entrySet).get());
    }

    @NonNull
    public static <V> Stream<V> noNull(@Nullable Stream<V> stream) {
        if (stream == null) {
            return Stream.empty();
        }
        return stream.filter(Objects::nonNull);
    }
}