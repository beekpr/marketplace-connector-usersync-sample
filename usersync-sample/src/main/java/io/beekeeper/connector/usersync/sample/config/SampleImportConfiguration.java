package io.beekeeper.connector.usersync.sample.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration for Sample connector tenant-specific settings.
 */
@Getter
@Setter
public class SampleImportConfiguration {
    static final int DEFAULT_PAGE_LIMIT = 50;

    private String baseUrl;

    private String clientId;

    private String clientSecret;

    private Integer pageLimit = DEFAULT_PAGE_LIMIT;

}
