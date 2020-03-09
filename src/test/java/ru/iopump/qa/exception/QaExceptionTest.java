package ru.iopump.qa.exception;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class QaExceptionTest {

    @Test
    public void testConstructors() {
        QaException exception = new QaException();
        assertThat(exception).isInstanceOf(RuntimeException.class);

        exception = new QaException("{} {}", "first");
        assertThat(exception).hasMessage("first {}");

        exception = new QaException("{} {}", new Exception(), null, null);
        assertThat(exception)
            .hasMessage("null null")
            .hasCauseInstanceOf(Exception.class);

        exception = new QaException(new Exception());
        assertThat(exception)
            .hasCauseInstanceOf(Exception.class);
    }
}