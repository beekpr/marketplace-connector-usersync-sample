package io.beekeeper.connector.usersync.sample.synchronization;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.beekeeper.marketplace.sdk.utils.http.oauth2.AccessToken;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Stub {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void stubGetToken() throws JsonProcessingException {
        final AccessToken token = AccessToken.builder()
            .accessToken("my-fun-token")
            .tokenType("elo")
            .build();

        stubFor(
            post(urlEqualTo("/auth/oauth/v2/token"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(OBJECT_MAPPER.writeValueAsString(token))
                )
        );
    }

    public static Returning<String> stubGetWorkers() {
        return response -> stubFor(
            get(urlPathEqualTo("/api/1/users"))
                .willReturn(aResponse().withBody(response).withStatus(200))
        );
    }

    public static void stubIncorrectCredentialsGetToken() throws IOException {
        stubFor(
            post(urlEqualTo("/auth/oauth/v2/token"))
                .willReturn(
                    aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"invalid_client\"}")
                )
        );
    }

    public static void stubGetWorkers500() {
        stubFor(
            get(urlPathEqualTo("/api/1/users"))
                .willReturn(aResponse().withBody("BUM").withStatus(500))
        );
    }

    public interface Returning<T> {
        void returning(T response);
    }
}
