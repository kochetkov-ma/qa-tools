package ru.iopump.qa.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import org.junit.Test;

public class ReflectionUtilTest {

    @Test
    public void getGenericTypes() throws NoSuchFieldException {
        assertThat(ReflectionUtil.getGenericTypes(Tmp.f("set"))).containsExactly(String.class);
        assertThat(ReflectionUtil.getGenericTypes(Tmp.f("queue"))).containsExactly(Integer.class);
        assertThat(ReflectionUtil.getGenericTypes(Tmp.f("collection"))).containsExactly(Long.class);
        assertThat(ReflectionUtil.getGenericTypes(Tmp.f("arrayList"))).containsExactly(Byte.class);
        assertThat(ReflectionUtil.getGenericTypes(Tmp.f("map"))).containsExactly(String.class, String.class);
        assertThat(ReflectionUtil.getGenericTypes(Tmp.f("optional"))).containsExactly(String.class);
    }

    @Test
    public void createImplementations() {
        assertThat(ReflectionUtil.createImplementations(ITmp.class, "not_exists")).isEmpty();
        assertThat(ReflectionUtil.createImplementations(ITmp.class, null)).isEmpty();
        assertThat(ReflectionUtil.createImplementations(ITmp.class, "ru.iopump.qa.util"))
            .hasSize(2)
            .hasOnlyElementsOfTypes(TmpOther.class, Tmp.class);
        assertThat(ReflectionUtil.createImplementations(ATmp.class, "ru.iopump.qa.util"))
            .hasSize(1)
            .hasOnlyElementsOfTypes(Tmp.class);
    }

    @Test
    public void findImplementations() {
        assertThat(ReflectionUtil.findImplementations(ITmp.class, "not_exists")).isEmpty();
        assertThat(ReflectionUtil.findImplementations(ITmp.class, null)).isEmpty();
        assertThat(ReflectionUtil.findImplementations(ITmp.class, "ru.iopump.qa.util"))
            .hasSize(2);
        assertThat(ReflectionUtil.findImplementations(ATmp.class, "ru.iopump.qa.util"))
            .hasSize(1);
    }

    @Test
    public void findOneClassBySimpleNameInJdk() {
        assertThat(ReflectionUtil.findOneClassBySimpleNameInJdk("Collections"))
            .hasValue(Collections.class);
        assertThat(ReflectionUtil.findOneClassBySimpleNameInJdk("not_exists"))
            .isEmpty();
        assertThat(ReflectionUtil.findOneClassBySimpleNameInJdk("ReflectionUtilTest"))
            .isEmpty();
    }

    private interface ITmp {
    }

    private static abstract class ATmp {
    }

    private static class TmpOther implements ITmp {
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static class Tmp extends ATmp implements ITmp {
        private Set<String> set;
        private Queue<Integer> queue;
        private Collection<Long> collection;
        private ArrayList<Byte> arrayList;
        private Map<String, String> map;
        private Optional<String> optional;

        public static Field f(String name) throws NoSuchFieldException {
            return Tmp.class.getDeclaredField(name);
        }
    }
}