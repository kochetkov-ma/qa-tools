package ru.iopump.qa.support.http;

import okhttp3.*;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestHtmlServerIT {
    @Rule
    public LocalSimpleHtmlServer.TestHtmlServer server = LocalSimpleHtmlServer.of().asTestRule();

    @Test
    public void publish() throws InterruptedException {
        Assertions.assertThat(server.getHtml()).isEqualTo(LocalSimpleHtmlServer.TestHtmlServer.HTML);

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(server.getUrl()).build();
        Call call = client.newCall(request);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Assertions.fail("localhost simple age must be available. But not.", e);
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Assertions.assertThat(response.body().string())
                        .contains(server.getHtml());
                countDownLatch.countDown();
            }
        });
        countDownLatch.await(1, TimeUnit.SECONDS);
    }
}