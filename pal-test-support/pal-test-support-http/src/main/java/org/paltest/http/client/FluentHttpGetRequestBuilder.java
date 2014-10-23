package org.paltest.http.client;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public class FluentHttpGetRequestBuilder<T extends FluentHttpGetRequestBuilder> extends FluentHttpRequestBuilder<T> {
    public static FluentHttpGetRequestBuilder get () {
        return new FluentHttpGetRequestBuilder();
    }

    @Override
    protected HttpRequestBase build(String url) {
        return new HttpGet(url);
    }
}
