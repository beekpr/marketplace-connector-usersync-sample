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

        // All users have to be sent to the observer
        observer.onNext(userProvider.fetchBatch());

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
