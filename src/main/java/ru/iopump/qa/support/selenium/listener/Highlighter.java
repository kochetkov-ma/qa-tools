package ru.iopump.qa.support.selenium.listener;

import java.io.Closeable;
import javax.annotation.Nullable;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Class for turn on/off highlighting element in active {@link WebElement}.
 */
public interface Highlighter extends Closeable {
    /**
     * Highlight element in driver and save this element for {@link #unhighlightPrev}.
     *
     * @param element WebDriver to Highlight.
     * @param driver  WebDriver with JS.
     * @return success action
     */
    boolean highlight(@Nullable WebElement element, @NonNull WebDriver driver);

    /**
     * Turn off highlighting for last saved element during {@link #highlight} or do nothing if no active element.
     *
     * @param currentElement WebDriver to Turn off highlighting.
     * @param driver         WebDriver with JS.
     * @return success action
     */
    boolean unhighlightPrev(@Nullable WebElement currentElement, @NonNull WebDriver driver);

    /**
     * Remove last Highlighted element and other cache.
     */
    void dropState();
}
