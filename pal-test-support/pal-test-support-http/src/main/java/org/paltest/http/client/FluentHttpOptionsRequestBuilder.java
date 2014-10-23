package org.paltest.http.client;

import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpRequestBase;

public class FluentHttpOptionsRequestBuilder<T extends FluentHttpOptionsRequestBuilder> extends FluentHttpRequestBuilder<T> {
    public static FluentHttpOptionsRequestBuilder options () {
        return new FluentHttpOptionsRequestBuilder();
    }
    @Override
    protected HttpRequestBase build(String url) {
        return new HttpOptions(url);
    }
}
