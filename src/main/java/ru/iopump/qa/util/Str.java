package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class Str {
    public static final String NULL_STR = "null";
    private static String OVERRIDE_NULL_STR = NULL_STR;

    /**
     * Change null representation of null {@link String}.
     *
     * @param nullStr New null representation of null {@link String}
     */
    public void setNullStr(@NonNull String nullStr) {
        synchronized (NULL_STR) {
            OVERRIDE_NULL_STR = nullStr;
        }
    }

    /**
     * Get null representation of null {@link String}.
     * Null-safe.
     *
     * @return null representation of null {@link String}
     */
    @NonNull
    public String nullStr() {
        synchronized (NULL_STR) {
            return OVERRIDE_NULL_STR;
        }
    }

    /**
     * Null safe formatter like in slf4j.
     *
     * @param slf4jMessagePattern Slf4j pattern with '{}'
     * @param args                Values
     * @return Formatted string
     * @see MessageFormatter#arrayFormat(String, Object[])
     */
    @NonNull
    public String format(@Nullable String slf4jMessagePattern, @Nullable Object... args) {
        if (slf4jMessagePattern == null) return nullStr();
        if (args == null) return slf4jMessagePattern;
        return MessageFormatter.arrayFormat(slf4jMessagePattern, args).getMessage();
    }

    /**
     * Process source string and return some result. If source string is not blank (or null or empty).
     *
     * @param value    source string
     * @param consumer source string processor
     * @return processed string and return some result.
     * @see StringUtils#isNotBlank(CharSequence)
     */
    @NonNull
    public <T> Optional<T> ifNotBlank(@Nullable CharSequence value, @Nullable Function<CharSequence, T> consumer) {
        return (StringUtils.isNotBlank(value) && consumer != null) ? Optional.ofNullable(consumer.apply(value)) : Optional.empty();
    }
    @NonNull
    public String toStr(@Nullable Object object) {
        return toString(object);
    }

    @NonNull
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String toString(@Nullable Object object) {
        if (object == null) return nullStr();
        if (object instanceof String) return (String)object;
        if (object instanceof Collection) return Arrays.toString(StreamUtil.stream((Collection) object).map(Str::toString).toArray());
        if (object instanceof Object[]) return Arrays.toString(StreamUtil.stream((Object[]) object).map(Str::toString).toArray());
        try {
            if (object.getClass().getMethod("toString").getDeclaringClass() != Object.class) return object.toString();
        } catch (NoSuchMethodException ignored) { /* nothing */}
        return ReflectionToStringBuilder.toString(object, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
