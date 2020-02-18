package ru.iopump.qa.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class SystemUtil {

    @Nullable
    public String get(@NonNull String propertyName) {
        String sys = System.getenv(propertyName);
        String env = System.getProperty(propertyName);
        return sys != null ? sys : env;
    }

    @Nullable
    public String getOfDefault(@NonNull String propertyName, @Nullable String defaultValue) {
        String tmp = get(propertyName);
        return tmp != null ? tmp : defaultValue;
    }
}