package org.paltest.http.client;

public class StringBodyContentBuilder extends BodyContentBuilder {
    public static StringBodyContentBuilder stringContent (String content) {
        return new StringBodyContentBuilder(content);
    }

    private String body;

    public StringBodyContentBuilder(String body) {
        this.body = body;
    }

    @Override
    byte[] build() {
        return body.getBytes();
    }
}
