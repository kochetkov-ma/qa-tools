package ru.iopump.qa.support.selenium.listener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.FieldSetter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("ConstantConditions")
public class SingleThreadHighlighterImplTest {

    private SingleThreadHighlighterImpl hHighlighter;
    private WebDriver driver;
    private WebElement element1;
    private WebDriver badDriver;
    private WebElement element2;

    @Before
    public void setUp() {
        hHighlighter = new SingleThreadHighlighterImpl();
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
    public void testException() throws NoSuchFieldException {
        final String prevElementField = "prevElement";
        assertThatThrownBy(() -> hHighlighter.highlight(element1, badDriver))
                .isInstanceOf(UnsupportedOperationException.class);
        FieldSetter.setField(hHighlighter, hHighlighter.getClass().getDeclaredField(prevElementField), element1);
        assertThatThrownBy(() -> hHighlighter.unhighlightPrev(element2, badDriver))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testHighlight() {
        assertThat(hHighlighter).extracting("borderStyle").isEqualTo("3px solid red");
        assertThat(hHighlighter).extracting("prevElement").isNull();
        hHighlighter.highlight(element1, driver);
        assertThat(hHighlighter).extracting("prevElement").isEqualTo(element1);
    }

    private interface WebDriverJs extends WebDriver, JavascriptExecutor {
    }
}