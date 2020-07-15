package io.beekeeper.connector.usersync.sample.e2e.recorded;

import io.beekeeper.connector.usersync.sample.ExampleUserRestService;
import io.beekeeper.connector.usersync.sample.SampleConnector;
import io.beekeeper.integration.connector.api.config.ConnectorConfigurationForTenant;
import io.beekeeper.integration.connector.api.config.GlobalConfiguration;
import io.beekeeper.integration.test.recording.UserSyncRecorder;
import io.beekeeper.integration.test.recording.UserSyncRecordingData;

import java.nio.file.Path;

public class TestRecording {

    public static void main(String[] args) throws Exception {
        final UserSyncRecorder userSyncRecorder = new UserSyncRecorder();
        final ExampleUserRestService sampleService = new ExampleUserRestService();
        sampleService.start();
        final var tenantConfiguration = configuration(
            sampleService.getBaseUrl(),
            sampleService.getClientId(),
            sampleService.getClientSecret()
        );
        System.out.println("port: " + sampleService.getBaseUrl());

        final UserSyncRecordingData userSyncRecordingData = UserSyncRecordingData.builder()
            .outputPath(Path.of("src/test/resources"))
            .expectedUserCount(2)
            .globalConfiguration(new GlobalConfiguration())
            .tenantConfiguration(tenantConfiguration)
            .tenantPropertyToAnonymize("clientSecret")
            .build();

        final SampleConnector connector = new SampleConnector();
        userSyncRecorder.performImportAndRecordNetworkCalls(connector, "happyPath", userSyncRecordingData);
        sampleService.stop();
    }

    private static ConnectorConfigurationForTenant configuration(String baseUrl, String clientId, String clientSecret) {
        final ConnectorConfigurationForTenant tenantConfiguration = new ConnectorConfigurationForTenant();
        tenantConfiguration.setProperty("baseUrl", baseUrl);
        tenantConfiguration.setProperty("clientId", clientId);
        tenantConfiguration.setProperty("clientSecret", clientSecret);
        return tenantConfiguration;
    }
}
