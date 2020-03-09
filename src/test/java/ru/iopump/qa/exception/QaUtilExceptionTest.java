package ru.iopump.qa.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class QaUtilExceptionTest {
    @Test
    public void testConstructors() {
        QaUtilException exception = new QaUtilException();
        assertThat(exception).isInstanceOf(RuntimeException.class);

        exception = new QaUtilException("{} {}", "first");
        assertThat(exception).hasMessage("first {}");

        exception = new QaUtilException("{} {}", new Exception(), null, null);
        assertThat(exception)
            .hasMessage("null null")
            .hasCauseInstanceOf(Exception.class);

        exception = new QaUtilException(new Exception());
        assertThat(exception)
            .hasCauseInstanceOf(Exception.class);
    }
}