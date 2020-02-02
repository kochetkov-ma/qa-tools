package ru.iopump.qa.support.http;

import com.sun.net.httpserver.HttpServer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ru.iopump.qa.support.http.LocalSimpleHtmlServer.TestHtmlServer.HTML;

/**
 * JDK http server. To provide simple html page on localhost.
 * This page can be gotten on local host via http get request on specified endpoint.
 * Provided Html page can be specified as well as port and path.
 * Use it in your unit (integration) tests with {@link org.openqa.selenium.WebDriver}.
 * Also pay attention on method {@link #asTestRule()}.
 */
@Slf4j
@ToString(exclude = "server")
public class LocalSimpleHtmlServer implements Closeable {
    private static final int DEFAULT_PORT = 8080;
    private final int port;
    private final String path;
    @Setter
    private Charset charset = StandardCharsets.UTF_8;
    @Getter(AccessLevel.PACKAGE)
    private HttpServer server;
    @Getter
    private boolean published;

    private LocalSimpleHtmlServer(int port, @Nullable String path) {
        this.port = port;
        this.path = Optional.ofNullable(path).orElse("/");
    }

    /**
     * New {@link LocalSimpleHtmlServer}.
     */
    public static LocalSimpleHtmlServer of(int port, String path) {
        return new LocalSimpleHtmlServer(port, path);
    }

    /**
     * New {@link LocalSimpleHtmlServer}.
     */
    public static LocalSimpleHtmlServer of(String path) {
        return of(DEFAULT_PORT, path);
    }

    /**
     * New {@link LocalSimpleHtmlServer}.
     */
    public static LocalSimpleHtmlServer of() {
        return of(null);
    }

    /**
     * Create junit rule from server with default html {@link TestHtmlServer#HTML}.
     *
     * @return New object provided junit test rule interface, contains this http server.
     * @see #publish(String)
     */
    public final TestHtmlServer asTestRule() {
        return asTestRule(HTML);
    }

    /**
     * Create junit rule from server.
     *
     * @param html see {@link #publish(String)}
     * @return New object provided junit test rule interface, contains this http server.
     */
    public final TestHtmlServer asTestRule(@NonNull String html) {
        return new TestHtmlServer(html);
    }

    /**
     * Publish HTML string on localhost http server with specified path and port.
     *
     * @param html Html page as string.
     */
    public synchronized void publish(@NonNull String html) {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
            log.info("[SIMPLE HTTP SERVER] Created");
        } catch (IOException e) {
            throw new RuntimeException("Error during creating JDK http-server " + toString(), e);
        }
        if (published) {
            server.removeContext(path);
        }
        server.createContext(path, httpExchange -> {
            log.debug("[SIMPLE HTTP SERVER] Get an request '{} from {}'",
                    httpExchange.getRequestMethod(),
                    httpExchange.getRequestURI());

            byte[] response = html.getBytes(charset);
            httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=" + charset.name());
            httpExchange.sendResponseHeaders(200, response.length);
            try (final OutputStream out = httpExchange.getResponseBody()) {
                out.write(response);
            }
            log.debug("[SIMPLE HTTP SERVER] Response has been prepared '{} from {}'",
                    httpExchange.getResponseHeaders(),
                    httpExchange.getResponseCode());

        });
        log.info("[SIMPLE HTTP SERVER] Handler created on http://localhost:{}{}", port, path);
        if (!published) {
            server.start();
            published = true;
            log.info("[SIMPLE HTTP SERVER] Started on http://localhost:{}{}", port, path);
        }
    }

    @Override
    public void close() {
        Optional.ofNullable(server)
                .ifPresent(s -> {
                    s.stop(1);
                    log.info("[SIMPLE HTTP SERVER] Closed {}", toString());
                });
    }

    @AllArgsConstructor
    @Setter
    @Getter
    public final class TestHtmlServer implements TestRule {
        static final String HTML = "<!DOCTYPE html> <html> <body> " +
                "<h2>Simple HTML</h2> <p>Simple buttons</p> " +
                "<div> <button>Button-1</button> <button>Button-2</button> </div> </body> </html>";
        private String html;

        public int getPort() {
            return port;
        }

        public String getPath() {
            return path;
        }

        public String getUrl() {
            return "http://localhost:" + port + path;
        }

        public void withHtml(String html) {
            this.html = html;
        }

        /**
         * Don't use it!
         */
        @Override
        @Deprecated
        public Statement apply(Statement base, Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    publish(html);
                    try {
                        base.evaluate();
                    } finally {
                        close();
                    }
                }
            };
        }
    }
}