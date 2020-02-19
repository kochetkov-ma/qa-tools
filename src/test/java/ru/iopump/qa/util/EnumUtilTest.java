package ru.iopump.qa.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;
import ru.iopump.qa.support.api.WithValue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EnumUtilTest {

    @Test
    public void getByValue() {
        assertThatThrownBy(() -> EnumUtil.getByValue(EnumTmp.class, "ONE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There are no enum items among '[ONE, TWO, THREE]' with value = 'ONE' ignore case");
        assertThatThrownBy(() -> EnumUtil.getByValue(EnumTmpInt.class, 4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There are no enum items among '[ONE, TWO, THREE]' with value = '4'");

        assertThat(EnumUtil.getByValue(EnumTmp.class, "ONE_VALUE")).isEqualTo(EnumTmp.ONE);
        assertThat(EnumUtil.getByValue(EnumTmp.class, "one_value")).isEqualTo(EnumTmp.ONE);
        assertThat(EnumUtil.getByValue(EnumTmpInt.class, 1)).isEqualTo(EnumTmpInt.ONE);
    }

    @Test
    public void getByName() {
        assertThatThrownBy(() -> EnumUtil.getByName(EnumTmp.class, "one_value"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("There are no enum items among '[ONE, TWO, THREE]' with name = 'one_value' ignore case");

        assertThat(EnumUtil.getByName(EnumTmp.class, "TWO")).isEqualTo(EnumTmp.TWO);
        assertThat(EnumUtil.getByName(EnumTmp.class, "two")).isEqualTo(EnumTmp.TWO);
    }

    @AllArgsConstructor
    @Getter
    private enum EnumTmp implements WithValue<String> {
        ONE("one_value"), TWO("two_value"), THREE("three_value");
        private final String value;
    }

    @AllArgsConstructor
    @Getter
    private enum EnumTmpInt implements WithValue<Integer> {
        ONE(1), TWO(2), THREE(3);
        private final Integer value;
    }
}