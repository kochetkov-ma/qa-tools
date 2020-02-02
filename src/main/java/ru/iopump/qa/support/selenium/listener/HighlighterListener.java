package ru.iopump.qa.support.selenium.listener;

import lombok.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverEventListener;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * {@link WebDriverEventListener} is intended to highlight current active element.
 * Use static constructor {@link #of(Highlighter)} and {@link #newSingleThreadHighlighterListener()}
 * with default {@link SingleThreadHighlighterImpl} highlighting strategy.
 */
@NotThreadSafe
public class HighlighterListener extends AbstractWebDriverEventListener {
    private final Highlighter highlighter;

    private HighlighterListener(@NonNull Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    public static WebDriverEventListener newSingleThreadHighlighterListener() {
        return new HighlighterListener(new SingleThreadHighlighterImpl());
    }

    public HighlighterListener of(Highlighter highlighter) {
        return new HighlighterListener(highlighter);
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        highlighter.unhighlightPrev(element, driver);
        highlighter.highlight(element, driver);
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        highlighter.dropState();
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        highlighter.dropState();
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        highlighter.dropState();
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        highlighter.dropState();
    }
}