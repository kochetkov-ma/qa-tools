package ru.iopump.qa.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StreamUtil {

    /**
     * Safe to create stream.
     */
    @SafeVarargs
    @NonNull
    public static <T> Stream<T> stream(@Nullable T... array) {
        if (array == null) {
            return Stream.empty();
        }
        return stream(Arrays.asList(array));
    }

    /**
     * Safe to create stream.
     */
    @NonNull
    public static <T> Stream<T> stream(@Nullable Iterable<T> iterable) {
        return StreamSupport.stream(Optional.ofNullable(iterable)
            .map(Iterable::spliterator).orElse(Spliterators.emptySpliterator()), false);
    }

    /**
     * Safe to create stream.
     */
    @NonNull
    public static <K, V> Stream<Map.Entry<K, V>> stream(@Nullable Map<K, V> map) {
        return stream(Optional.ofNullable(map).map(Map::entrySet).get());
    }

    /**
     * Safe to create stream.
     */
    @NonNull
    public static <V> Stream<V> noNull(@Nullable Stream<V> stream) {
        if (stream == null) {
            return Stream.empty();
        }
        return stream.filter(Objects::nonNull);
    }
}