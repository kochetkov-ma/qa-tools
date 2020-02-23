package ru.iopump.qa.util;

import lombok.Cleanup;
import org.junit.Test;
import ru.iopump.qa.exception.QaUtilException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResourceUtilTest {

    @SuppressWarnings("RedundantThrows")
    @Test
    public void getResourceAsStream() throws URISyntaxException, IOException {
        // From user dir
        @Cleanup InputStream io0 = ResourceUtil.getResourceAsStream("src/test/resources/resource-3.txt");
        assertThat(io0).hasContent("3");
        // From user dir
        URL url = Thread.currentThread().getContextClassLoader().getResource("resource-3.txt");
        Path path = Paths.get(Objects.requireNonNull(url).toURI());
        @Cleanup InputStream io1 = ResourceUtil.getResourceAsStream(path.toAbsolutePath().toString());
        assertThat(io1).hasContent("3");
        // From resource dir in classpath
        @Cleanup InputStream io2 = ResourceUtil.getResourceAsStream("resource-3.txt");
        assertThat(io2).hasContent("3");
        // From resource dir in classpath
        @Cleanup InputStream io3 = ResourceUtil.getResourceAsStream("dir/resource-2.txt");
        assertThat(io3).hasContent("2");
        // From class dir in classpath
        @Cleanup InputStream io4 = ResourceUtil.getResourceAsStream("ResourceUtilTest.class");
        assertThat(io4).isNotNull();

        assertThatThrownBy(() -> ResourceUtil.getResourceAsStream("not_exists"))
                .isInstanceOf(QaUtilException.class)
                .hasMessageContaining("Cannot find resource 'not_exists'");
    }
}