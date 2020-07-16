package io.beekeeper.connector.usersync.sample;

import io.beekeeper.connector.usersync.sample.config.SampleImportConfiguration;
import io.beekeeper.integration.connector.api.exception.ConfigurationException;
import io.beekeeper.integration.connector.api.exception.ImportProviderException;
import io.beekeeper.marketplace.sdk.utils.http.oauth2.OAuth2ProviderException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SampleUserProvider {

    final SampleApi sampleApi;

    public SampleUserProvider(SampleImportConfiguration sampleImportConfiguration) {
        sampleApi = new ApiClientFactory(
                sampleImportConfiguration.getBaseUrl(),
                sampleImportConfiguration.getClientId(),
                sampleImportConfiguration.getClientSecret()
        )
            .build();
    }

    public List<SampleUser> fetchBatch() throws ImportProviderException {
        try {
            final Call<ResponseBody> workers = sampleApi.getUsers();
            final Response<ResponseBody> response = workers.execute();
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    return Collections.emptyList();
                }
                throw new ImportProviderException(
                        "Fetching workers failed because of an error: "
                            + response.errorBody().toString()
                            + ", code: "
                            + response.code()
                );
            }
            final ResponseBody responseBody = response.body();

            return Optional.ofNullable(responseBody)
                .map(ResponseBody::getUsers)
                .orElse(Collections.emptyList());
        } catch (OAuth2ProviderException e) {
            throw new ConfigurationException("Incorrect credentials", e);
        } catch (IOException e) {
            throw new ImportProviderException("Fetching users failed", e);
        }
    }
}
