package org.paltest.http.client;

import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;

public class FluentHttpHeadRequestBuilder<T extends FluentHttpHeadRequestBuilder> extends FluentHttpRequestBuilder<T> {
    public static FluentHttpHeadRequestBuilder head () {
        return new FluentHttpHeadRequestBuilder();
    }
    @Override
    protected HttpRequestBase build(String url) {
        return new HttpHead(url);
    }
}
