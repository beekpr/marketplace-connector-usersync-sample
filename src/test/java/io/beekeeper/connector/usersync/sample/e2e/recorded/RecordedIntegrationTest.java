package io.beekeeper.connector.usersync.sample.e2e.recorded;

import io.beekeeper.connector.usersync.sample.SampleConnector;
import io.beekeeper.integration.test.UserUpdate;
import io.beekeeper.integration.test.WrapperTestSupport;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(userUpdate.getExternalUserId()).isEqualTo("value1");
        assertThat(userUpdate.getFirstName()).isEqualTo("value2");
        assertThat(userUpdate.getLastName()).isEqualTo("bla");
        assertThat(userUpdate.getLogin()).isEqualTo("value1");
        assertThat(userUpdate.getPosition()).isEqualTo("programmer");
        assertThat(userUpdate.isSuspended()).isFalse();
    }
}
