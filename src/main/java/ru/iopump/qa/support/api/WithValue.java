package ru.iopump.qa.support.api;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import ru.iopump.qa.util.Str;

import javax.annotation.Nullable;

public interface WithValue<T> {

    @Nullable
    T getValue();

    default boolean hasValue(@Nullable T candidateValue) {
        if (getValue() instanceof String || candidateValue instanceof String) {
            return StringUtils.equalsAnyIgnoreCase(Str.toStr(candidateValue), Str.toStr(getValue()));
        }
        return Objects.equal(getValue(), candidateValue);
    }
}
