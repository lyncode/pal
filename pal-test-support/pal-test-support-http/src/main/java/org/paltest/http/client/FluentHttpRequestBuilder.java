package org.paltest.http.client;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.ArrayList;
import java.util.List;

public abstract class FluentHttpRequestBuilder<M extends FluentHttpRequestBuilder> {
    private final List<Header> headers = new ArrayList<Header>();
    private UrlBuilder url;

    protected M self() {
        return (M) this;
    }

    public M with (HeaderBuilder header) {
        this.headers.add(header.build());
        return self();
    }

    public M to (UrlBuilder builder) {
        this.url = builder;
        return self();
    }

    protected abstract HttpRequestBase build(String baseUrl);

    HttpRequestBase request (String baseUrl) {
        HttpRequestBase build = build(url.build(baseUrl));
        for (Header header : headers) {
            build.addHeader(header);
        }
        return build;
    }


}
