package org.paltest.http.client;

import org.apache.commons.io.IOUtils;
import org.paltest.exceptions.PalTestException;

import java.io.IOException;
import java.io.InputStream;

public class StreamBodyContentBuilder extends BodyContentBuilder {
    public static StreamBodyContentBuilder stream (InputStream stream) {
        return new StreamBodyContentBuilder(stream);
    }

    private final InputStream inputStream;

    public StreamBodyContentBuilder(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    byte[] build() {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new PalTestException(e);
        }
    }
}
