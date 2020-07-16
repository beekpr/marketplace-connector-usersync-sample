package io.beekeeper.connector.usersync.sample;

import io.beekeeper.marketplace.sdk.utils.http.oauth2.AccessTokenEndpoint;
import io.beekeeper.marketplace.sdk.utils.http.oauth2.AccessTokenRequest;
import io.beekeeper.marketplace.sdk.utils.http.oauth2.ClientCredentialsTokenRequest;
import io.beekeeper.marketplace.sdk.utils.http.oauth2.OAuth2AuthorizationRequestInterceptor;
import lombok.AllArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class ApiClientFactory {
    final String baseUrlString;
    final String clientId;
    final String clientSecret;

    public SampleApi build() {
        final HttpUrl baseUrl = baseUrl(baseUrlString);
        final OAuth2AuthorizationRequestInterceptor oAuthInterceptor =
            getOAuthInterceptor(clientId, clientSecret, baseUrl);

        OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(oAuthInterceptor)
            .build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .client(httpClient)
            .build();

        return retrofit.create(SampleApi.class);
    }

    private HttpUrl baseUrl(String baseUrl) {
        return HttpUrl.parse(baseUrl);
    }

    private OAuth2AuthorizationRequestInterceptor getOAuthInterceptor(
            String clientId,
            String clientSecret,
            HttpUrl hostname
    ) {
        HttpUrl tokenEndpointUrl = hostname.resolve("/auth/oauth/v2/token");

        final AccessTokenEndpoint accessTokenEndpoint = new AccessTokenEndpoint(tokenEndpointUrl);
        AccessTokenRequest tokenRequest = ClientCredentialsTokenRequest.builder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();

        return new OAuth2AuthorizationRequestInterceptor(accessTokenEndpoint, tokenRequest);
    }
}
