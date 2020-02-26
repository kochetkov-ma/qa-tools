package ru.iopump.qa.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class VarUtilTest {

    @Before
    public void before() {
        System.clearProperty("OS");
    }

    @After
    public void after() {
        System.clearProperty("OS");
    }

    @Test
    public void getAll() {
        System.setProperty("TEST_SYS", "TEST_OS_FROM_SYS_ENV");

        final Map<String, String> result = VarUtil.getAll();
        log.info("RESULT: {}\n", Str.toPrettyString(result));
        assertThat(result).isNotEmpty()
                .containsEntry("OS", System.getenv("OS"))
                .containsEntry("TEST_SYS", "TEST_OS_FROM_SYS_ENV");
    }

    @Test
    public void get() {
        Optional<String> envProp = VarUtil.get("OS");
        assertThat(envProp).isNotEmpty();

        System.setProperty("OS", "TEST_OS_FROM_SYS_ENV");
        envProp = VarUtil.get("OS");
        assertThat(envProp).hasValue("TEST_OS_FROM_SYS_ENV");
    }

    @Test
    public void getOfDefault() {
        assertThat(VarUtil.getOfDefault("NOT_EXISTS", "DEFAULT_VALUE"))
                .isEqualTo("DEFAULT_VALUE");
    }
}