package io.beekeeper.connector.usersync.sample;

import io.beekeeper.directorySync.mapping.field.external.ExternalField;
import io.beekeeper.directorySync.mapping.field.external.IExternalField;
import io.beekeeper.integration.connector.usersync.api.ConnectorDefaultMapping;

public class SampleDefaultMapping implements ConnectorDefaultMapping {
    @Override
    public IExternalField getExternalUserId() {
        return ExternalField.builder()
            .externalFieldName("id")
            .build();
    }

    @Override
    public IExternalField getIsSuspended() {
        return ExternalField.builder()
            .externalFieldName("suspended")
            .build();
    }

    @Override
    public IExternalField getLogin() {
        return ExternalField.builder()
            .externalFieldName("id")
            .build();
    }

    @Override
    public IExternalField getFirstName() {
        return ExternalField.builder()
            .externalFieldName("firstName")
            .build();
    }

    @Override
    public IExternalField getLastName() {
        return ExternalField.builder()
            .externalFieldName("lastName")
            .build();
    }

    @Override
    public IExternalField getPosition() {
        return ExternalField.builder()
            .externalFieldName("position")
            .build();
    }
}
