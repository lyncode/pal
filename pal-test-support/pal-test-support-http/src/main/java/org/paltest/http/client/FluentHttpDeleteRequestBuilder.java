package org.paltest.http.client;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;

public class FluentHttpDeleteRequestBuilder<T extends FluentHttpDeleteRequestBuilder> extends FluentHttpRequestBuilder<T> {
    public static FluentHttpDeleteRequestBuilder delete () {
        return new FluentHttpDeleteRequestBuilder();
    }

    @Override
    protected HttpRequestBase build(String url) {
        return new HttpDelete(url);
    }
}
