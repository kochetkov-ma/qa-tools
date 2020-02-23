package ru.iopump.qa.support.http;

import okhttp3.*;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"EmptyMethod", "ConstantConditions"})
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
        localSimpleHtmlServer.publish(HTML);
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(URL).build();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Assertions.fail("localhost simple age must be available. But not.", e);
                        countDownLatch.countDown();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Assertions.assertThat(response.body().string())
                                .contains(HTML);
                        countDownLatch.countDown();
                    }
                });
        countDownLatch.await(1, TimeUnit.SECONDS);
    }

    @Test
    public void close() {
    }

    @Test
    public void isPublished() {
    }

    @Test
    public void testToString() {
    }
}