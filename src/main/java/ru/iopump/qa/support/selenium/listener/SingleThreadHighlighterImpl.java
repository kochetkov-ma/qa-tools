package ru.iopump.qa.support.selenium.listener;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Objects;

import static ru.iopump.qa.support.selenium.listener.HUtil.*;

/**
 * Single thread and single {@link WebDriver} implementation.
 */
@NotThreadSafe
public class SingleThreadHighlighterImpl implements Highlighter {

    private final String borderStyle;
    private WebElement prevElement;

    public SingleThreadHighlighterImpl(int px, String color) {
        Objects.requireNonNull(color, "Color cannot be null");

        borderStyle = px + " px solid " + color.toLowerCase();
    }

    public SingleThreadHighlighterImpl() {
        this(3, "red");
    }

    public boolean highlight(WebElement element, WebDriver driver) {
        if (element != null && driver != null) {

            /* Get current element border style */
            final String currentStyle = exec(PROP, element, driver).toLowerCase();

            /* Do only if element hasn't already been highlighted */
            if (!currentStyle.contains(borderStyle)) {

                /* Execute script and save to cache */
                exec(jsBorder(borderStyle), element, driver);
                prevElement = element;
                return true;
            }
        }
        return false;
    }

    public boolean unhighlightPrev(WebElement elementWillBeHighlighted, WebDriver driver) {
        /* Do only for new elements */
        if (prevElement != null && elementWillBeHighlighted != prevElement && driver != null) {

            /* Get previous element border style */
            final String lastBorderStyle = exec(PROP, prevElement, driver).toLowerCase();

            /* Do only if element has already been highlighted */
            if (!lastBorderStyle.contains(borderStyle)) {

                /* Calculate reverted style and execute script */
                final String revertedStyle = lastBorderStyle.replaceAll(borderStyle, "");
                exec(jsBorder(revertedStyle), prevElement, driver);
                dropState();
                return true;
            }
        }
        return false;
    }

    public void dropState() {
        prevElement = null;
    }

    @Override
    public void close() {
        dropState();
    }
}
