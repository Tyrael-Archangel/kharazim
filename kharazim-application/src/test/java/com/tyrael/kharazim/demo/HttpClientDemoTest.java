package com.tyrael.kharazim.demo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
public class HttpClientDemoTest {

    @Test
    public void runHttpClient() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://icd11.pumch.cn/api/services/app/Linearization/GetMMSDetailFromId?refId=1435254666&releaseId=2023-01&language="))
                .GET()
                .build();
        HttpResponse<String> httpResponse = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode());
        System.out.println(httpResponse.body());
        System.out.println(httpResponse.headers());
    }

}
