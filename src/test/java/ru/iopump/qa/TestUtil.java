package ru.iopump.qa;

import static ru.iopump.qa.TestConstants.PITEST_ENV_BOOT;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.iopump.qa.util.VarUtil;

/**
 * Internal testing utilities.
 */
@UtilityClass
public class TestUtil {
    /**
     * Check if Program is executed by Mutation Test Gradle Plugin 'PiTest'.
     * It's workaround.
     * Because PiTest process some operation with 'null' in arrays and collections another way than JDK11 compiler.
     * I don't know why ...
     */
    public static boolean isPiTest() {
        return VarUtil.get(PITEST_ENV_BOOT)
            .map(value -> StringUtils.containsIgnoreCase(value, "org.pitest.coverage.execute.CoverageMinion"))
            .orElse(false);
    }
}
