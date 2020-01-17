package ru.iopump.qa.support.selenium.listener;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.Closeable;

/**
 * Class for turn on/off highlighting element in active {@link WebElement}.
 */
public interface Highlighter extends Closeable {
    /**
     * Highlight element in driver and save this element for {@link #unhighlight}.
     *  @param element WebDriver to Highlight.
     * @param driver  WebDriver with JS.
     * @return
     */
    boolean highlight(WebElement element, WebDriver driver);

    /**
     * Turn off highlighting for last saved element during {@link #highlight} or do nothing if no active element.
     *  @param currentElement WebDriver to Turn off highlighting.
     * @param driver         WebDriver with JS.
     * @return
     */
    boolean unhighlight(WebElement currentElement, WebDriver driver);

    /**
     * Remove last Highlighted element and other cache.
     */
    void dropState();
}
