package ru.iopump.qa.support.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.net.BindException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.iopump.qa.exception.QaException;

@SuppressWarnings( {"EmptyMethod", "ConstantConditions"})
public class LocalSimpleHtmlServerIT {

    private static final String URL = "http://localhost:8080";
    private static final String HTML = "<!DOCTYPE html> <html> <body> " +
        "<h2>Simple HTML</h2> <p>Simple buttons</p> " +
        "<div> <button>Button-1</button> <button>Button-2</button> </div> </body> </html>";
    private LocalSimpleHtmlServer localSimpleHtmlServer;

    @Before
    public void setUp() {
        this.localSimpleHtmlServer = LocalSimpleHtmlServer.of();
    }

    @After
    public void tearDown() {
        localSimpleHtmlServer.close();
    }

    @Test
    public void publish() throws InterruptedException {
        assertThat(localSimpleHtmlServer.isPublished()).isFalse();
        localSimpleHtmlServer.publish("</html>");
        assertThat(localSimpleHtmlServer.isPublished()).isTrue();
        localSimpleHtmlServer.publish(HTML);
        assertThat(localSimpleHtmlServer.isPublished()).isTrue();
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request)
            .enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Assertions.fail("localhost simple age must be available. But not.", e);
                    countDownLatch.countDown();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    assertThat(response.body().string())
                        .contains(HTML);
                    countDownLatch.countDown();
                }
            });
        countDownLatch.await(1, TimeUnit.SECONDS);


        try (LocalSimpleHtmlServer server = LocalSimpleHtmlServer.of(5555, "/path")) {
            server.publish(HTML);
            request = new Request.Builder().url("http://localhost:5555/path").build();
            final CountDownLatch countDownLatch1 = new CountDownLatch(1);
            client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Assertions.fail("localhost simple age must be available. But not.", e);
                        countDownLatch1.countDown();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        assertThat(response.body().string())
                            .contains(HTML);
                        countDownLatch1.countDown();
                    }
                });
            countDownLatch1.await(1, TimeUnit.SECONDS);
        }
    }

    @Test
    public void publishFail() {
        localSimpleHtmlServer.publish(HTML);
        assertThatThrownBy(() -> LocalSimpleHtmlServer.of().publish(""))
            .isInstanceOf(QaException.class)
            .hasCauseInstanceOf(BindException.class);
    }

    @Test
    public void testToString() {
        assertThat(localSimpleHtmlServer.toString())
            .contains("port=8080", "path=/", "charset=UTF-8", "published=false");
        localSimpleHtmlServer.publish("");

        assertThat(localSimpleHtmlServer.toString())
            .contains("port=8080", "path=/", "charset=UTF-8", "published=true");
    }
}