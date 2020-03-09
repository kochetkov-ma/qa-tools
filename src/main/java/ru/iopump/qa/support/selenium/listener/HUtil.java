package ru.iopump.qa.support.selenium.listener;

import java.util.Objects;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.iopump.qa.util.Str;

/**
 * Internal.
 */
final class HUtil {
    static final String PROP = "arguments[0].style.border";

    private HUtil() {
        throw new AssertionError("Utility class");
    }

    static String exec(String script, WebElement element, WebDriver driver) {
        if (!(driver instanceof JavascriptExecutor)) {
            throw new UnsupportedOperationException(
                Str.format("Your WebDriver '{}' doesn't support executing js-script. You haven't use this listener!",
                    driver)
            );
        }
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            return Objects.toString(executor.executeScript(script, element), "");
        } catch (RuntimeException ignore) { // NOPMD - It's ok
            return "";
        }
    }

    static String jsBorder(String borderStyle) {
        return PROP + "='" + borderStyle + "'";
    }
}