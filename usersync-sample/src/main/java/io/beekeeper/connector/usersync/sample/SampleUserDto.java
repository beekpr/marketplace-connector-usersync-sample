package io.beekeeper.connector.usersync.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for Sample API user response.
 * Represents the raw data structure from the external API.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleUserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean suspended;
    private String position;

}
