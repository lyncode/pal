package org.paltest.http.client;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;

public class FluentHttpPutRequestBuilder<T extends FluentHttpPutRequestBuilder> extends FluentHttpWithContentRequestBuilder<T> {
    public static FluentHttpPutRequestBuilder put () {
        return new FluentHttpPutRequestBuilder();
    }
    @Override
    HttpEntityEnclosingRequestBase buildEnclosable(String url) {
        return new HttpPut(url);
    }
}
