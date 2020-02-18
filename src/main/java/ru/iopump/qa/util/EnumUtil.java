package ru.iopump.qa.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import ru.iopump.qa.support.api.WithValue;

import javax.annotation.Nullable;

import static ru.iopump.qa.util.Str.format;
import static ru.iopump.qa.util.Str.toStr;

public class EnumUtil {

    @NonNull
    public <T extends Enum<T> & WithValue<V>, V> T getByValue(@NonNull Class<T> enumClass,
                                                              @Nullable V candidate) {
        final T[] values = enumClass.getEnumConstants();
        return StreamUtil.stream(values)
                .filter(i -> i.hasValue(candidate))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("There are no enum items among '{}' " +
                        "with string value = '{}' ignore case", toStr(values), toStr(candidate))));
    }

    @NonNull
    public <T extends Enum<T>> T getByName(@NonNull Class<T> enumClass,
                                           @Nullable String candidateString) {
        final T[] values = enumClass.getEnumConstants();
        return StreamUtil.stream(values)
                .filter(i -> StringUtils.equalsAnyIgnoreCase(i.name(), candidateString))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("There are no enum items among '{}' " +
                        "with name = '{}' ignore case", toStr(values), candidateString)));
    }
}
