package io.beekeeper.connector.usersync.sample.synchronization;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.beekeeper.connector.usersync.sample.SampleConnector;
import io.beekeeper.integration.connector.api.config.ConnectorConfigurationForTenant;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.connector.api.exception.ConfigurationException;
import io.beekeeper.integration.connector.api.exception.ImportProviderException;
import io.beekeeper.integration.connector.usersync.api.UserImportConfiguration;
import io.beekeeper.integration.connector.usersync.api.UserImportObserver;
import io.beekeeper.integration.connector.usersync.api.data.UserData;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.*;

public class SampleUserConnectorTest {


    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(9000), false);

    @Rule
    public ResourceFile getWorkersResponseJson = new ResourceFile(
            "exampleUserRestService/__files/getUsersResponse.json"
    );

    private final GlobalConfiguration globalConfiguration = new GlobalConfiguration();

    @Test
    public void canRetrieveUsers() throws Exception {
        // GIVEN
        Stub.stubGetToken();
        stubGetWorkers();

        UserImportObserver importObserver = mock(UserImportObserver.class);
        ArgumentCaptor<List<UserData>> userDataCaptor = ArgumentCaptor.forClass(List.class);

        // WHEN
        SampleConnector sampleConnector = new SampleConnector();
        sampleConnector.initialize(globalConfiguration);
        sampleConnector.fetchUsers(buildImportConfiguration(), importObserver);

        // THEN
        verify(importObserver, times(1)).onNext(userDataCaptor.capture());
        Assertions.assertThat(userDataCaptor.getValue()).hasSize(2);
    }

    @Test(expected = ConfigurationException.class)
    public void throwsConfigurationExceptionWhenIncorrectCredentials() throws Exception {
        // GIVEN
        Stub.stubIncorrectCredentialsGetToken();
        stubGetWorkers();

        UserImportObserver importObserver = mock(UserImportObserver.class);

        // WHEN
        SampleConnector sampleConnector = new SampleConnector();
        sampleConnector.initialize(globalConfiguration);
        sampleConnector.fetchUsers(buildImportConfiguration(), importObserver);
    }

    @Test(expected = ImportProviderException.class)
    public void throwsImportProviderExceptionWhenApiFails() throws Exception {
        // GIVEN
        Stub.stubGetToken();
        Stub.stubGetWorkers500();

        UserImportObserver importObserver = mock(UserImportObserver.class);

        // WHEN
        SampleConnector sampleConnector = new SampleConnector();
        sampleConnector.initialize(globalConfiguration);
        sampleConnector.fetchUsers(buildImportConfiguration(), importObserver);
    }

    private void stubGetWorkers() throws Exception {
        Stub.stubGetWorkers().returning(getWorkersResponseJson.getContent());
    }

    private UserImportConfiguration buildImportConfiguration() {
        ConnectorConfigurationForTenant tenantConfiguration = new ConnectorConfigurationForTenant();
        tenantConfiguration.setProperty("baseUrl", "http://localhost:9000");
        tenantConfiguration.setProperty("clientId", "clientId");
        tenantConfiguration.setProperty("clientSecret", "clientSecret");
        return UserImportConfiguration.builder().connectorConfigurationForTenant(tenantConfiguration).build();
    }

}
