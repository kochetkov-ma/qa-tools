package ru.iopump.qa.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.iopump.qa.util.Str.*;

@Slf4j
public class StrTest {

    @Test
    public void testNullStr() {
        setNullStrDefault(NULL_STR_DEFAULT);
        assertThat(nullStr()).isEqualTo("null");
        setNullStrDefault("");
        assertThat(nullStr()).isEqualTo("");
    }

    @Test
    public void testFormat() {
        assertThat(format(null, "1")).isEqualTo("null");
        assertThat(format("{}", (Object[]) null)).isEqualTo("{}");
        assertThat(format("{}", new Object[0])).isEqualTo("{}");
        assertThat(format("{}{}{}", 1)).isEqualTo("1{}{}");
        assertThat(format("{}", 1, 2)).isEqualTo("1");
        assertThat(format("not null '{}' null '{}'", 1, null))
                .isEqualTo("not null '1' null 'null'");
        assertThat(format("date is '{}'", LocalDate.of(1, 1, 1)))
                .isEqualTo("date is '" + LocalDate.of(1, 1, 1) + "'");
    }

    @Test
    public void testIfNotBlank() {
        assertThat(ifNotBlank(null, (str) -> 1)).isEmpty();
        assertThat(ifNotBlank("  ", (str) -> 1)).isEmpty();
        assertThat(ifNotBlank(null, (str) -> null)).isEmpty();
        assertThat(ifNotBlank("  ", (str) -> null)).isEmpty();
        assertThat(ifNotBlank("not_blank", (str) -> null)).isEmpty();
        assertThat(ifNotBlank("not_blank", null)).isEmpty();

        assertThat(ifNotBlank("not_blank", (str) -> str)).hasValue("not_blank");
        assertThat(ifNotBlank("not_blank", (str) -> str + "_other")).hasValue("not_blank_other");
    }

    @Test
    public void testToString() {
        Object obj;
        assertThat(Str.toStr(null)).isEqualTo("null");

        obj = "";
        assertThat(Str.toStr(obj)).isEqualTo("");

        obj = new Object();
        assertThat(Str.toStr(obj)).isEqualTo("Object()");

        obj = new Integer[]{1, 2, 3, 4, 5};
        assertThat(Str.toStr(obj)).isEqualTo("[1, 2, 3, 4, 5]");

        obj = ImmutableMap.of(1, 2, 3, 4);
        assertThat(Str.toStr(obj)).isEqualTo("{1=2, 3=4}");

        obj = new Item3();
        assertThat(Str.toStr(obj)).isEqualTo("StrTest.Item3(integers3=[1, 2])");

        obj = new Item1();
        assertThat(Str.toStr(obj))
                .isEqualTo("StrTest.Item1(" +
                        "collection1=[StrTest.Item2(collection2=[StrTest.Item3(integers3=[1, 2]), StrTest.Item3(integers3=[1, 2])]), " +
                        "StrTest.Item2(collection2=[StrTest.Item3(integers3=[1, 2]), StrTest.Item3(integers3=[1, 2])])], " +
                        "name1=testing)");

        obj = new Item4();
        assertThat(Str.toStr(obj)).isEqualTo("StrTest.Item4(name4=test,integers3={1,2})");
    }

    @Test
    public void testToPrettyStringCollectionAndArray() {
        final UUID uuid = UUID.randomUUID();
        final List list = Lists.newArrayList("VALUE" , new Object(), 1000, null, uuid);
        String result = Str.toPrettyString(list);
        log.info("RESULT: {}\n", result);
        assertThat(result).contains("VALUE", "Object()", "1000", "null", Str.toStr(uuid));

        result = Str.toPrettyString(list.toArray());
        log.info("RESULT: {}\n", result);
        assertThat(result).contains("VALUE", "Object()", "1000", "null", Str.toStr(uuid));
    }

    @Test
    public void testToPrettyStringMap() {
        final UUID uuid = UUID.randomUUID();
        final Map map = Maps.newHashMap();
        map.put("KEY", "VALUE");
        map.put(uuid, new Object());
        map.put(1, 1000);
        map.put("KEY_LONG_KEY_LONG_KEY_LONG_KEY_LONG", null);
        map.put(null, null);
        map.put("KEY_LONG", "VALUE_LONG_VALUE_LONG_VALUE_LONG_VALUE_LONG");
        final String result = Str.toPrettyString(map);
        log.info("RESULT: {}\n", result);
        assertThat(result).contains("null : null",
                "KEY : VALUE",
                uuid + " : java.lang.Object",
                "1 : 1000",
                "KEY_LONG_KEY_LONG_KEY_LONG_KEY_LONG : null",
                "KEY_LONG : VALUE_LONG_VALUE_LONG_VALUE_LONG_VALUE_LONG");
    }

    private String iHash(Object obj) {
        return Integer.toHexString(System.identityHashCode(obj));
    }

    @ToString
    private final static class Item1 {
        private Collection<Item2> collection1 = ImmutableList.of(new Item2(), new Item2());
        private String name1 = "testing";
    }

    @ToString
    private final static class Item2 {
        private Collection<Item3> collection2 = ImmutableList.of(new Item3(), new Item3());
    }

    @ToString
    private final static class Item3 {
        private Integer[] integers3 = new Integer[]{1, 2};
    }

    private final static class Item4 {
        private String name4 = "test";
        private Integer[] integers3 = new Integer[]{1, 2};
    }
}