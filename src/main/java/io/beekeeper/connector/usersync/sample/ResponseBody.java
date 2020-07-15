package io.beekeeper.connector.usersync.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBody {
    @Getter
    @JsonProperty("content")
    private List<SampleUser> users;
}
