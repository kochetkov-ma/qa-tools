package ru.iopump.qa.util;

import java.util.Optional;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ClassUtil {

    /**
     * Null-safe class cast.
     *
     * @param object        Object to cast. May be null.
     * @param expectedClass Target class.
     * @param message       Message when exception
     * @param <TypeT>       Target type.
     * @return object with expected type.
     * @throws ClassCastException If cast is impossible.
     */
    public static <TypeT> Optional<TypeT> cast(@Nullable Object object, @NonNull Class<TypeT> expectedClass, @Nullable String message) {
        if (object == null) {
            return Optional.empty();
        }
        if (expectedClass.isInstance(object)) {
            return Optional.of(expectedClass.cast(object));
        } else {
            throw new ClassCastException(
                Str.format("Cannot cast class '{}' with value '{}' to class '{}'.\nMessage: '{}'",
                    getClass(object),
                    Str.toString(object),
                    expectedClass,
                    StringUtils.defaultIfBlank(message, "empty")));
        }
    }

    /**
     * Get class or {@link Object#getClass()} if null.
     */
    @NonNull
    public static Class<?> getClass(@Nullable Object value) {
        if (value == null) {
            return Object.class;
        }
        return value.getClass();
    }
}