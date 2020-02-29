package ru.iopump.qa.util;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * System and Environment variables utility class.
 *
 * @see System#getenv(String)
 * @see System#getProperty(String)
 */
@SuppressWarnings("WeakerAccess")
@UtilityClass
public class VarUtil {

    /**
     * Get all System and Environment variables.
     *
     * @see System#getenv()
     * @see System#getProperties()
     */
    public static Map<String, String> getAll() {
        return ImmutableMap.<String, String>builder()
                .putAll(System.getenv())
                .putAll(System.getProperties().entrySet().stream()
                        .collect(Collectors.toMap(e->Str.toStr(e.getKey()), e->Str.toStr(e.getValue()))))
                .build();
    }

    /**
     * Get variable from System and Environment variables by key.
     *
     * @see System#getenv(String)
     * @see System#getProperty(String)
     */
    public static Optional<String> get(@NonNull String propertyNameAkaKey) {
        final String sys = System.getProperty(propertyNameAkaKey);
        final String env = System.getenv(propertyNameAkaKey);
        return sys != null ? Optional.of(sys) : Optional.ofNullable(env);
    }

    /**
     * Get variable from System and Environment variables by key.
     *
     * @see System#getenv(String)
     * @see System#getProperty(String)
     */
    @Nullable
    public static String getOrDefault(@NonNull String propertyNameAkaKey, @Nullable String defaultValue) {
        return get(propertyNameAkaKey).orElse(defaultValue);
    }
}