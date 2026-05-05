package io.beekeeper.connector.usersync.sample;

import io.beekeeper.connector.usersync.sample.config.SampleImportConfiguration;
import io.beekeeper.integration.connector.api.exception.ConfigurationException;
import io.beekeeper.integration.connector.api.exception.ImportProviderException;
import io.beekeeper.integration.connector.usersync.api.data.UserData;
import io.beekeeper.marketplace.sdk.utils.http.oauth2.OAuth2ProviderException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Provider for fetching users from the Sample API with pagination support.
 * Manages offset-based pagination state and batches user data retrieval.
 */
public class SampleUserProvider {

    final SampleApi sampleApi;
    final Integer pageLimit;
    private Integer currentOffset;
    private Boolean hasMore;

    public SampleUserProvider(SampleImportConfiguration sampleImportConfiguration) {
        sampleApi = new ApiClientFactory(
                sampleImportConfiguration.getBaseUrl(),
                sampleImportConfiguration.getClientId(),
                sampleImportConfiguration.getClientSecret()
        ).build();

        this.pageLimit = sampleImportConfiguration.getPageLimit();
        this.hasMore = true;
        this.currentOffset = 0;
    }

    public List<UserData> fetchBatch() throws ImportProviderException, ConfigurationException {
        if (!hasMore) {
            return Collections.emptyList();
        }

        try {
            final Call<ResponseBody> usersCall = sampleApi.getUsers(pageLimit, currentOffset);
            final Response<ResponseBody> response = usersCall.execute();

            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    hasMore = false;
                    return Collections.emptyList();
                }
                throw new ImportProviderException(
                        String.format(
                            "Fetching users failed because of an error: {} , code: {} "
                                + response.errorBody().toString()
                                +
                                +response.code()
                        )
                );
            }

            final ResponseBody responseBody = response.body();
            List<SampleUserDto> userDtos = Optional.ofNullable(responseBody)
                .map(ResponseBody::getUsers)
                .orElse(Collections.emptyList());

            hasMore = userDtos.size() >= pageLimit;
            currentOffset += pageLimit;

            List<UserData> transformedUsers = new ArrayList<>();
            for (SampleUserDto userDto : userDtos) {
                transformedUsers.add(SampleUserForBeekeeperMapping.fromUserDto(userDto));
            }

            return transformedUsers;

        } catch (OAuth2ProviderException e) {
            throw new ConfigurationException("Incorrect credentials", e);
        } catch (IOException e) {
            throw new ImportProviderException("Fetching users failed", e);
        }
    }
}
