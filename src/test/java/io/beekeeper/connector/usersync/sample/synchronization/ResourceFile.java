package io.beekeeper.connector.usersync.sample.synchronization;

import org.apache.commons.io.IOUtils;
import org.junit.rules.ExternalResource;

import java.io.IOException;
import java.nio.charset.Charset;

public class ResourceFile extends ExternalResource {
    private final String path;

    public ResourceFile(String res) {
        this.path = res;
    }

    public static String asString(String path) {
        try {
            return IOUtils.toString(
                ResourceFile.class.getClassLoader().getResourceAsStream(path),
                Charset.defaultCharset().toString()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getContent() throws IOException {
        return asString(path);
    }
}
