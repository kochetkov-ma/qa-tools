package ru.iopump.qa.exception;

import ru.iopump.qa.util.Str;

/**
 * Formatted {@link RuntimeException}.
 *
 * @see Str#format(String, Object...)
 */
public class QaException extends RuntimeException {

    private static final long serialVersionUID = -7658126557325361545L;

    public QaException() {
    }

    /**
     * Formatted constructor.
     *
     * @see Str#format(String, Object...)
     */
    public QaException(String message, Object... objects) {
        super(Str.format(message, objects));
    }

    /**
     * Formatted constructor.
     *
     * @see Str#format(String, Object...)
     */
    public QaException(String message, Throwable cause, Object... objects) {
        super(Str.format(message, objects), cause);
    }

    public QaException(Throwable cause) {
        super(cause);
    }
}
