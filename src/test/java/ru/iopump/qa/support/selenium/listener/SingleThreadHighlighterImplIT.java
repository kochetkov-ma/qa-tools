package ru.iopump.qa.support.selenium.listener;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testcontainers.containers.BrowserWebDriverContainer;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer;
import ru.iopump.qa.support.http.LocalSimpleHtmlServer.TestHtmlServer;

import java.io.File;

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
                    .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, new File("./vnc"));

    @Test
    public void openPageAndDemonstrateHighlighting() {
        final WebDriver driver = chrome.getWebDriver();
        final String url = "http://" + DOCKER_HOST_MACHINE_HOSTNAME + ":" + server.getPort() + server.getPath();
        driver.get(url);
        final String pageHtml = driver.getPageSource();
        Assertions.assertThat(pageHtml.strip())
                .contains("<button>Button-2</button>");
        Assertions.assertThat(server.getHtml())
                .contains("<button>Button-2</button>");
    }
}