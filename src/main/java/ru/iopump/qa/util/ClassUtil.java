package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

@UtilityClass
public class ClassUtil {

    public <CLASS> CLASS cast(@Nullable Object object, @NonNull Class<CLASS> expectedClass, @Nullable String message) {
        if (object == null) return null;
        if (expectedClass.isInstance(object)) return expectedClass.cast(object);
        else {
            throw new ClassCastException(
                    Str.format("Cannot cast class '{}' with value '{}' to class '{}'.\nMessage: '{}'",
                            getClass(object),
                            Str.toString(object),
                            expectedClass,
                            StringUtils.defaultIfBlank(message, "empty")));
        }
    }

    public boolean instanceOf(@Nullable Class<?> childType, @NonNull Class<?> baseType) {
        if (baseType == Object.class || childType == null) {
            return true;
        }
        return baseType.isAssignableFrom(childType);
    }

    /**
     * Get class or {@link Class<Object>} if null.
     */
    @NonNull
    public static Class<?> getClass(@Nullable Object value) {
        if (value == null) {
            return Object.class;
        }
        return value.getClass();
    }
}