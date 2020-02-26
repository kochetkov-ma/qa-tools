package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.iopump.qa.support.api.WithValue;

import javax.annotation.Nullable;

import static ru.iopump.qa.util.Str.format;
import static ru.iopump.qa.util.Str.toStr;

@UtilityClass
public class EnumUtil {

    /**
     * Find enum implemented interface {@link WithValue} by value.
     * This method walk through all enum constants, get value, compare this value with expected value.
     * And return suitable enum constant or {@link IllegalArgumentException}.
     * Ignore case if expected type is {@link String}.
     *
     * @param enumClass     Enum class
     * @param expectedValue Searching value
     * @param <T>           Enum type
     * @param <V>           Value type
     * @return Concrete enum constant.
     * @throws IllegalArgumentException Not exists
     */
    @NonNull
    public static <T extends Enum<T> & WithValue<V>, V> T getByValue(@NonNull Class<T> enumClass,
                                                                     @Nullable V expectedValue) {
        final T[] values = enumClass.getEnumConstants();
        return StreamUtil.stream(values)
                .filter(i -> i.hasValue(expectedValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("There are no enum items among '{}' " +
                        "with value = '{}'{}", toStr(values), toStr(expectedValue), expectedValue instanceof String ? " ignore case" : "")));
    }

    /**
     * Find enum by name (ignore case).
     * This method walk through all enum constants, get value, compare this value with expected value.
     * And return suitable enum constant or {@link IllegalArgumentException}.
     *
     * @param enumClass        Enum class
     * @param expectedEnumName Searching enum {@link Enum#name()} usually equals {@link Enum#toString()}
     * @param <T>              Enum type
     * @return Concrete enum constant.
     * @throws IllegalArgumentException Not exists
     */
    @NonNull
    public static <T extends Enum<T>> T getByName(@NonNull Class<T> enumClass,
                                                  @Nullable String expectedEnumName) {
        final T[] values = enumClass.getEnumConstants();
        return StreamUtil.stream(values)
                .filter(i -> StringUtils.equalsAnyIgnoreCase(i.name(), expectedEnumName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("There are no enum items among '{}' " +
                        "with name = '{}' ignore case", toStr(values), expectedEnumName)));
    }
}