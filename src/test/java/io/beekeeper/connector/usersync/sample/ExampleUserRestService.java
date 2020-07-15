package io.beekeeper.connector.usersync.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import io.beekeeper.marketplace.sdk.utils.http.oauth2.AccessToken;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class ExampleUserRestService {

    public final static String CLIENT_ID = "6976";
    public final static String CLIENT_SECRET = "9866";
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final WireMockServer wireMock;

    public static void main(String[] args) throws JsonProcessingException {
        ExampleUserRestService exampleService = new ExampleUserRestService();
        exampleService.start();
        System.out.println("Example User Import REST Service is available at " + exampleService.getBaseUrl());
    }

    public ExampleUserRestService() throws JsonProcessingException {
        wireMock = new WireMockServer(
                options()
                    .dynamicPort()
                    .extensions(new ResponseTemplateTransformer(false))
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

    public String getClientId() {
        return CLIENT_ID;
    }

    public String getClientSecret() {
        return CLIENT_SECRET;
    }

    private void stubExternalApi() {
        wireMock.stubFor(
            get(urlPathEqualTo("/api/1/users"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBodyFile("getUsersResponse.json")
                        .withTransformers("response-template")
                )
        );
    }

    private void stubGetToken() throws JsonProcessingException {
        final AccessToken token = AccessToken.builder()
            .accessToken("my-fun-token")
            .tokenType("elo")
            .build();

        wireMock.stubFor(
            post(urlEqualTo("/auth/oauth/v2/token"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(OBJECT_MAPPER.writeValueAsString(token))
                )
        );
    }

}
