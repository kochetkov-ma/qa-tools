package ru.iopump.qa.exception;

import ru.iopump.qa.util.Str;

/**
 * Formatted {@link RuntimeException}.
 *
 * @see Str#format(String, Object...)
 */
@SuppressWarnings("unused")
public class QaUtilException extends RuntimeException {
    private static final long serialVersionUID = -6426093358209017743L;

    public QaUtilException() {
    }

    /**
     * Formatted constructor.
     *
     * @see Str#format(String, Object...)
     */
    public QaUtilException(String message, Object... objects) {
        super(Str.format(message, objects));
    }

    /**
     * Formatted constructor.
     *
     * @see Str#format(String, Object...)
     */
    public QaUtilException(String message, Throwable cause, Object... objects) {
        super(Str.format(message, objects), cause);
    }

    public QaUtilException(Throwable cause) {
        super(cause);
    }

    public QaUtilException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
