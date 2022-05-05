package io.beekeeper.connector.usersync.sample.e2e.recorded;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import io.beekeeper.connector.usersync.sample.SampleConnector;
import io.beekeeper.integration.test.UserUpdate;
import io.beekeeper.integration.test.WrapperTestSupport;

public class RecordedIntegrationTest {
    @Test
    public void importWithMockedDataShouldReturnExpectedUsers() throws Exception {
        // Given
        final SampleConnector sampleConnector = new SampleConnector();

        // When
        List<UserUpdate> updateList = new WrapperTestSupport()
            .performImport(sampleConnector, "happyPath");

        // Then
        assertThat(updateList).hasSize(2);

        final UserUpdate userUpdate = updateList.get(0);
        assertThat(userUpdate.getExternalUserId()).isEqualTo("someid");
        assertThat(userUpdate.getFirstName()).isEqualTo("winnie");
        assertThat(userUpdate.getLastName()).isEqualTo("thepooh");
        assertThat(userUpdate.getLogin()).isEqualTo("someid");
        assertThat(userUpdate.getPosition()).isEqualTo("teddybear");
        assertThat(userUpdate.isSuspended()).isFalse();
    }
}
