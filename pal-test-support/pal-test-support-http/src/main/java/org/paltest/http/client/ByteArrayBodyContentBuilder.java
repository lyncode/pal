package org.paltest.http.client;

public class ByteArrayBodyContentBuilder extends BodyContentBuilder {
    private final byte[] body;

    public ByteArrayBodyContentBuilder(byte[] body) {
        this.body = body;
    }

    @Override
    byte[] build() {
        return body;
    }
}
