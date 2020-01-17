package ru.iopump.qa.support.selenium.listener;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SingleThreadHighlighterImplTest {

    private SingleThreadHighlighterImpl hHighlighter;
    private WebDriver driver;
    private WebElement element1;
    private WebElement element2;
    private WebDriver badDriver;

    @Before
    public void setUp() {
        hHighlighter = Mockito.spy(new SingleThreadHighlighterImpl());
        /* mocking */
        badDriver = Mockito.mock(WebDriver.class);
        driver = Mockito.mock(WebDriverJs.class);
        element1 = Mockito.mock(WebElement.class);
        element2 = Mockito.mock(WebElement.class);
    }

    @After
    public void tearDown() {
        hHighlighter.close();
    }

    @Test
    @Ignore
    public void testException() {
        assertThatThrownBy(() -> hHighlighter.highlight(element1, badDriver))
                .isInstanceOf(UnsupportedOperationException.class);

        assertThatThrownBy(() -> hHighlighter.unhighlight(element1, badDriver))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @Ignore
    public void testHighlight() {
        assertThat(hHighlighter).extracting("borderStyle").isEqualTo("3 px solid red");
        assertThat(hHighlighter).extracting("prevElement").isNull();

        hHighlighter.highlight(element1, driver);

        assertThat(hHighlighter).extracting("prevElement").isEqualTo(element1);
    }

    @Test
    @Ignore
    public void testUnhighlight() {
    }

    @Test
    @Ignore
    public void testDropState() {
    }

    private interface WebDriverJs extends WebDriver, JavascriptExecutor {
    }
}