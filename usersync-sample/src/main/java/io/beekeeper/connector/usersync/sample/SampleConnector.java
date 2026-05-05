package io.beekeeper.connector.usersync.sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.beekeeper.connector.usersync.sample.config.SampleImportConfiguration;
import io.beekeeper.integration.connector.api.config.ConnectorConfigurationForTenant;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.api.exception.ConfigurationException;
import io.beekeeper.integration.connector.api.exception.ImportProviderException;
import io.beekeeper.integration.connector.usersync.api.ConnectorDefaultMapping;
import io.beekeeper.integration.connector.usersync.api.UserImportConfiguration;
import io.beekeeper.integration.connector.usersync.api.UserImportConnector;
import io.beekeeper.integration.connector.usersync.api.UserImportObserver;
import io.beekeeper.integration.connector.usersync.api.UserImportResult;
import io.beekeeper.integration.connector.usersync.api.data.UserData;

import java.util.List;

/**
 * Main connector implementation for Sample API user synchronization.
 * Orchestrates the user import flow with pagination support.
 */
public class SampleConnector implements UserImportConnector {

    private final ObjectMapper objectMapper;

    public SampleConnector() {
        this.objectMapper = buildObjectMapper();
    }

    @Override
    public void initialize(GlobalConfiguration globalConfiguration) {
    }

    @Override
    public UserImportResult fetchUsers(UserImportConfiguration importConfiguration, UserImportObserver observer)
            throws ConfigurationException,
                ImportProviderException {
        ConnectorConfigurationForTenant tenantConfiguration = importConfiguration.getConnectorConfigurationForTenant();

        SampleImportConfiguration sampleImportConfiguration =
            this.objectMapper.convertValue(tenantConfiguration.getProperties(), SampleImportConfiguration.class);

        SampleUserProvider userProvider = new SampleUserProvider(sampleImportConfiguration);

        List<UserData> batch;
        while (!(batch = userProvider.fetchBatch()).isEmpty()) {
            observer.onNext(batch);
        }

        return new UserImportResult();
    }

    @Override
    public ConnectorDefaultMapping getDefaultMapping() {
        return new SampleDefaultMapping();
    }

    private ObjectMapper buildObjectMapper() {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
