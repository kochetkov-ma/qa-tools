package ru.iopump.qa.support.selenium.listener;

import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer.TestHtmlServer;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.iopump.qa.TestConstants.DOCKER_HOST_MACHINE_HOSTNAME;

public class SingleThreadHighlighterImplIT {
    private static final String HTML = "<!DOCTYPE html> <html> <body> " +
            "<h2>Simple HTML</h2> <p>Simple buttons</p> " +
            "<div> <button>Button-1</button> <button>Button-2</button> </div> </body> </html>";

    @Rule
    public TestHtmlServer server = LocalSimpleHtmlServer.of().asTestRule(HTML);

    @Rule
    @SuppressWarnings("rawtypes")
    public BrowserWebDriverContainer chrome =
            new BrowserWebDriverContainer()
                    .withCapabilities(new ChromeOptions())
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, new File("./build/vnc"));

    @Test
    public void openPageAndDemonstrateHighlighting() {
        final EventFiringWebDriver driver = new EventFiringWebDriver(chrome.getWebDriver());
        driver.register(HighlighterListener.newSingleThreadHighlighterListener());

        final String url = "http://" + DOCKER_HOST_MACHINE_HOSTNAME + ":" + server.getPort() + server.getPath();
        driver.get(url);
        final String pageHtml = driver.getPageSource();
        assertThat(pageHtml.strip()).contains("<button>Button-1</button>");
        assertThat(server.getHtml()).contains("<button>Button-2</button>");

        final List<WebElement> webElements = driver.findElements(By.tagName("button"));
        assertThat(webElements).hasSize(2);

        webElements.get(0).click();
        String style1 = webElements.get(0).getAttribute("style");
        assertThat(style1).contains("border: 3px solid red");

        webElements.get(1).click();
        style1 = webElements.get(0).getAttribute("style");
        String style2 = webElements.get(1).getAttribute("style");
        assertThat(style1).doesNotContain("3px solid red");
        assertThat(style2).contains("border: 3px solid red");
    }
}