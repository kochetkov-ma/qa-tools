package ru.iopump.qa.support.api;

import javax.annotation.Nullable;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Indicate that this object has name. Provides methods {@link #getName()} and {@link #hasName(String)}
 */
public interface WithName {

    /**
     * Get name.
     *
     * @return Object name.
     */
    @NonNull
    String getName();

    /**
     * Check name ignore case.
     *
     * @param expectedEqualsNameIgnoreCase Candidate string.
     * @return true - this object has passed name.
     */
    default boolean hasName(@Nullable String expectedEqualsNameIgnoreCase) {
        return getName().equalsIgnoreCase(ObjectUtils.defaultIfNull(expectedEqualsNameIgnoreCase, ""));
    }
}
