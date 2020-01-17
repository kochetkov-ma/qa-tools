package ru.iopump.qa.support.selenium.listener;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverEventListener;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class HighlighterListener extends AbstractWebDriverEventListener {
    private final Highlighter highlighter;
    public HighlighterListener(Highlighter highlighter) { this.highlighter = highlighter; }
    public static WebDriverEventListener newSingleThreadHighlighterListener() {
        return new HighlighterListener(new SingleThreadHighlighterImpl());
    }
    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        highlighter.unhighlight(element, driver);
        highlighter.highlight(element, driver); }
    @Override
    public void beforeNavigateTo(String url, WebDriver driver) { highlighter.dropState(); }
    @Override
    public void beforeNavigateBack(WebDriver driver) { highlighter.dropState(); }
    @Override
    public void beforeNavigateForward(WebDriver driver) { highlighter.dropState(); }
    @Override
    public void beforeNavigateRefresh(WebDriver driver) { highlighter.dropState(); }
}