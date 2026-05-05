package io.beekeeper.connector.usersync.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * Wrapper for Sample API paginated response.
 * Deserializes the JSON response containing user data.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBody {
    @Getter
    @JsonProperty("content")
    private List<SampleUserDto> users;
}
