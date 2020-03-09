package ru.iopump.qa.support.api;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import ru.iopump.qa.util.Str;

/**
 * This object has some value and can give it. In common situation it's a {@link String} but it may be any other {@link Object}.
 * And this object has special method {@link #hasValue} to check corresponding with this value and candidate value
 * with special logic if at least one of these objects is {@link String}.
 */
public interface WithValue<T> {

    /**
     * This object has some value and can give it. In common situation it's a {@link String} but it may be any other {@link Object}.
     * And this object has special method {@link #hasValue} to check corresponding with this value and candidate value
     * with special logic if at least one of these objects is {@link String}.
     *
     * @return Objects value.
     */
    @Nullable
    T getValue();

    /**
     * Smart check value if equals to {@link #getValue()}. For string use {@link StringUtils#equalsAnyIgnoreCase}
     *
     * @param candidateValue Candidate object or {@link String}
     * @return true - candidateValue corresponds to {@link #getValue()}.
     */
    default boolean hasValue(@Nullable T candidateValue) {
        if (getValue() instanceof String || candidateValue instanceof String) {
            return StringUtils.equalsAnyIgnoreCase(Str.toStr(candidateValue), Str.toStr(getValue()));
        }
        return Objects.equal(getValue(), candidateValue);
    }
}
