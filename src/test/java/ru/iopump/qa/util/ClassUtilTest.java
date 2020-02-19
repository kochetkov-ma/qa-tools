package ru.iopump.qa.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Optional;

public class ClassUtilTest {

    @Test
    public void cast() {
        Optional<CharSequence> res = ClassUtil.cast("string", CharSequence.class, "String to CharSequence");
        Assertions.assertThat(res).containsInstanceOf(CharSequence.class);
        Assertions.assertThatThrownBy(() -> ClassUtil.cast(new Object(), String.class, "Object to String"))
                .isInstanceOf(ClassCastException.class)
                .hasMessageContaining("Cannot cast class")
                .hasMessageContaining("Object to String");
    }
}