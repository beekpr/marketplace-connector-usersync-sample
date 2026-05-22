package io.beekeeper.connector.usersync.sample;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.beekeeper.integration.connector.usersync.api.data.UserData;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Transformation class that converts Sample API user DTOs into Beekeeper-compatible format.
 * Extends UserData to provide the required properties for the Beekeeper system.
 */
@Getter
@Setter
public class SampleUserForBeekeeperMapping extends UserData {

    @Override
    @JsonAnySetter
    public void setProperty(String attr, @Nullable Object value) {
        super.setProperty(attr, value);
    }

    public static SampleUserForBeekeeperMapping fromUserDto(@NonNull SampleUserDto userDto) {
        SampleUserForBeekeeperMapping mappedUser = new SampleUserForBeekeeperMapping();

        mappedUser.setProperty("id", userDto.getId());
        mappedUser.setProperty("firstName", userDto.getFirstName());
        mappedUser.setProperty("lastName", userDto.getLastName());
        mappedUser.setProperty("email", userDto.getEmail());
        mappedUser.setProperty("suspended", userDto.getSuspended());
        mappedUser.setProperty("position", userDto.getPosition());

        return mappedUser;
    }
}
