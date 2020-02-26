package ru.iopump.qa.util;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class StreamUtilTest {

    @Test
    public void testStream() {
        Supplier<Stream<Integer>> stream = () -> StreamUtil.stream(Lists.newArrayList(null,1,2,3,4,null));
        Assertions.assertThat(stream.get())
                .hasSize(6)
                .containsOnly(null,1,2,3,4,null);
        Assertions.assertThat(StreamUtil.noNull(stream.get()))
                .hasSize(4)
                .containsOnly(1,2,3,4);
    }
}