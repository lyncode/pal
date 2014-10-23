package org.paltest.http.client;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;

public abstract class FluentHttpWithContentRequestBuilder<T extends FluentHttpWithContentRequestBuilder> extends FluentHttpRequestBuilder<T> {
    private byte[] content;

    @Override
    protected HttpRequestBase build(String url) {
        HttpEntityEnclosingRequestBase requestBase = buildEnclosable(url);
        requestBase.setEntity(new ByteArrayEntity(content));
        return requestBase;
    }

    abstract HttpEntityEnclosingRequestBase buildEnclosable (String url);

    public FluentHttpRequestBuilder with(BodyContentBuilder content) {
        this.content = content.build();
        return self();
    }
}
