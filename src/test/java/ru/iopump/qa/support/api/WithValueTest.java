package ru.iopump.qa.support.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class WithValueTest {

    @Test
    public void hasValue() {
        WithValue withValue = () -> "string";
        assertThat(withValue.hasValue("StRiNg")).isTrue();
        assertThat(withValue.hasValue("StRiN")).isFalse();
        assertThat(withValue.hasValue(1)).isFalse();
        assertThat(withValue.hasValue(null)).isFalse();

        withValue = () -> "1";
        assertThat(withValue.hasValue(1)).isTrue();
        assertThat(withValue.hasValue(2)).isFalse();
        withValue = () -> "true";
        assertThat(withValue.hasValue(true)).isTrue();
        assertThat(withValue.hasValue(false)).isFalse();

        withValue = () -> null;
        assertThat(withValue.hasValue(null)).isTrue();
        assertThat(withValue.hasValue(1)).isFalse();

        withValue = () -> 1;
        assertThat(withValue.hasValue(1)).isTrue();
        assertThat(withValue.hasValue(2)).isFalse();

        Object obj = new Object();
        withValue = () -> obj;
        assertThat(withValue.hasValue(obj)).isTrue();
        assertThat(withValue.hasValue(new Object())).isFalse();
    }
}