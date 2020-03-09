package ru.iopump.qa.support.selenium.listener;

import static ru.iopump.qa.support.selenium.listener.HUtil.PROP;
import static ru.iopump.qa.support.selenium.listener.HUtil.exec;
import static ru.iopump.qa.support.selenium.listener.HUtil.jsBorder;

import java.util.Locale;
import java.util.Objects;
import javax.annotation.concurrent.NotThreadSafe;
import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Single thread and single {@link WebDriver} implementation.
 */
@NotThreadSafe
public class SingleThreadHighlighterImpl implements Highlighter {

    private final String borderStyle;
    private WebElement prevElement;

    /**
     * Constructor.
     */
    public SingleThreadHighlighterImpl(int px, String color) {
        Objects.requireNonNull(color, "Color cannot be null");

        borderStyle = px + "px solid " + color.toLowerCase(Locale.getDefault());
    }

    /**
     * Constructor.
     */
    public SingleThreadHighlighterImpl() {
        this(3, "red");
    }

    @Override
    public boolean highlight(WebElement element, WebDriver driver) {
        if (element != null && driver != null) {

            /* Get current element border style */
            final String currentStyle = exec(PROP, element, driver).toLowerCase(Locale.getDefault());

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

    @Override
    public boolean unhighlightPrev(WebElement elementWillBeHighlighted, WebDriver driver) {
        /* Do only for new elements */
        if (prevElement != null && ObjectUtils.notEqual(elementWillBeHighlighted, prevElement) && driver != null) {

            /* Get previous element border style */
            final String lastBorderStyle = exec(PROP, prevElement, driver).toLowerCase(Locale.getDefault());

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

    @Override
    public void dropState() {
        prevElement = null; //NOPMD - null is OK
    }

    @Override
    public void close() {
        dropState();
    }
}
