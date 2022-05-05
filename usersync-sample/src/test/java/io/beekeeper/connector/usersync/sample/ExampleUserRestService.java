package io.beekeeper.connector.usersync.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import io.beekeeper.marketplace.sdk.utils.http.oauth2.AccessToken;

// this class is a mock of an external rest service
public class ExampleUserRestService {

    public final static String CLIENT_ID = "6976";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final WireMockServer wireMock;

    public static void main(String[] args) throws JsonProcessingException {
        ExampleUserRestService exampleService = new ExampleUserRestService();
        exampleService.start();
        System.out.println("Example User Import REST Service is available at " + exampleService.getBaseUrl());
    }

    public ExampleUserRestService() throws JsonProcessingException {
        wireMock = new WireMockServer(
                WireMockConfiguration.options()
                    .dynamicPort()
                    .withRootDirectory("src/test/resources/exampleUserRestService")
        );
        stubExternalApi();
        stubGetToken();
    }

    public void start() {
        wireMock.start();
    }

    public void stop() {
        wireMock.stop();
    }

    public String getBaseUrl() {
        return wireMock.baseUrl();
    }

    private void stubExternalApi() {
        wireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/api/1/users"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withBodyFile("getUsersResponse.json")
                )
        );
    }

    private void stubGetToken() throws JsonProcessingException {
        final AccessToken token = AccessToken.builder()
            .accessToken("my-fun-token")
            .tokenType("elo")
            .build();

        wireMock.stubFor(
            WireMock.post(WireMock.urlEqualTo("/auth/oauth/v2/token"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(OBJECT_MAPPER.writeValueAsString(token))
                )
        );
    }

}