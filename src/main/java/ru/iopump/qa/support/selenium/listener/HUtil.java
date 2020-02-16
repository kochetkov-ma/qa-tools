package ru.iopump.qa.support.selenium.listener;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Internal.
 */
final class HUtil {
    static final String PROP = "arguments[0].style.border";
    private HUtil() { throw new AssertionError("Utility class"); }
    static String exec(String script, WebElement element, WebDriver driver) {
        if (!(driver instanceof JavascriptExecutor)) {
            throw new UnsupportedOperationException(
                    format("Your WebDriver '%s' doesn't support executing js-script. You haven't use this listener!",
                            driver)
            );
        }
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            return Objects.toString(executor.executeScript(script, element), "");
        } catch (Throwable throwable) {
            return "";
        }
    }
    static String jsBorder(String borderStyle) { return PROP + "='" + borderStyle + "'"; }
}