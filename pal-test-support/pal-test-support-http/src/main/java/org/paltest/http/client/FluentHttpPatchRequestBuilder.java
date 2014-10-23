package org.paltest.http.client;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPatch;

public class FluentHttpPatchRequestBuilder<T extends FluentHttpPatchRequestBuilder> extends FluentHttpWithContentRequestBuilder<T> {
    public static FluentHttpPatchRequestBuilder patch () {
        return new FluentHttpPatchRequestBuilder();
    }

    @Override
    HttpEntityEnclosingRequestBase buildEnclosable(String url) {
        return new HttpPatch(url);
    }
}
