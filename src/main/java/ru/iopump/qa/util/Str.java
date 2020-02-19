package ru.iopump.qa.util;

import com.google.common.collect.Iterables;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class Str {
    public static final Integer MAP_FORMAT_MAX_ALIGN_DEFAULT = 60;
    public static final String NULL_STR_DEFAULT = "null";

    private volatile static String NULL_STR = NULL_STR_DEFAULT;
    private volatile static int MAP_FORMAT_MAX_ALIGN = MAP_FORMAT_MAX_ALIGN_DEFAULT;

    public static void setMapFormatMaxAlign(int align) {
        MAP_FORMAT_MAX_ALIGN = align;
    }

    /**
     * Change null representation of null {@link String}.
     *
     * @param nullStrDefault New null representation of null {@link String}
     */
    public static void setNullStrDefault(@NonNull String nullStrDefault) {
        NULL_STR = nullStrDefault;
    }

    /**
     * Get null representation of null {@link String}.
     * Null-safe.
     *
     * @return null representation of null {@link String}
     */
    @NonNull
    public static String nullStr() {
        return NULL_STR;
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
    public static String format(@Nullable String slf4jMessagePattern, Object... args) {
        if (slf4jMessagePattern == null) return nullStr();
        if (ArrayUtils.isEmpty(args)) return slf4jMessagePattern;
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
    public static <T> Optional<T> ifNotBlank(@Nullable CharSequence value, @Nullable Function<CharSequence, T> consumer) {
        return (StringUtils.isNotBlank(value) && consumer != null) ? Optional.ofNullable(consumer.apply(value)) : Optional.empty();
    }

    @NonNull
    public static String toStr(@Nullable Object object) {
        return toString(object);
    }

    public static String toPrettyString(@Nullable Object... array) {
        if (array == null) return "";
        return toPrettyString(Arrays.asList(array));
    }

    @NonNull
    public static String toPrettyString(@Nullable Map<?, ?> map) {
        if (map == null) return "";
        final Map<String, String> printableMap = map.keySet().stream().collect(Collectors.toMap(Str::toStr, Str::toStr));
        int maxKeyLength = printableMap.keySet().stream().map(String::length).max(Comparator.naturalOrder())
                .orElse(10);
        if (maxKeyLength > MAP_FORMAT_MAX_ALIGN) maxKeyLength = MAP_FORMAT_MAX_ALIGN;
        final int maxKeyLengthFinal = maxKeyLength;
        return "Size: " + map.size() +
                StreamUtil.stream(map).map(e -> String.format("%" + maxKeyLengthFinal + "s : %s", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(System.lineSeparator(), System.lineSeparator(), ""));
    }

    public static String toPrettyString(@Nullable Iterable<?> objects) {
        if (objects == null) return "";
        return "Size: " + Iterables.size(objects) + StreamUtil.stream(objects).map(Str::toStr)
                .collect(Collectors.joining(System.lineSeparator(), System.lineSeparator(), ""));
    }

    @NonNull
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String toString(@Nullable Object object) {
        if (object == null) return nullStr();
        if (object instanceof String) return (String) object;
        try {
            if (object.getClass().getMethod("toString").getDeclaringClass() != Object.class) return object.toString();
        } catch (NoSuchMethodException ignored) { /* nothing */}
        if (object instanceof Collection)
            return Arrays.toString(StreamUtil.stream((Collection) object).map(Str::toString).toArray());
        if (object instanceof Object[])
            return Arrays.toString(StreamUtil.stream((Object[]) object).map(Str::toString).toArray());
        return ReflectionToStringBuilder.toString(object, QaSimpleToStringStyle.INSTANCE);
    }

    private static final class QaSimpleToStringStyle extends ToStringStyle {
        private static final ToStringStyle INSTANCE = new QaSimpleToStringStyle();
        private static final long serialVersionUID = 1L;

        private QaSimpleToStringStyle() {
            super();
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            this.setContentStart("(");
            this.setContentEnd(")");
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}
