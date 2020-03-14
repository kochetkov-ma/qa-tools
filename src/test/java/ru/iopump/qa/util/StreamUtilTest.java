package ru.iopump.qa.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;
import java.util.stream.Stream;
import org.assertj.core.util.Lists;
import org.junit.Test;

public class StreamUtilTest {

    @Test
    public void testStream() {
        Supplier<Stream<Integer>> stream = () -> StreamUtil.stream(Lists.newArrayList(null, 1, 2, 3, 4, null));
        assertThat(stream.get())
            .hasSize(6)
            .containsOnly(null, 1, 2, 3, 4, null);

        assertThat(StreamUtil.noNull(stream.get()))
            .hasSize(4)
            .containsOnly(1, 2, 3, 4);
    }

    @Test
    public void testStreamNull() {
        assertThat(StreamUtil.stream((Object[])null))
            .isEmpty();

        assertThat(StreamUtil.noNull(null))
            .isEmpty();
    }
}