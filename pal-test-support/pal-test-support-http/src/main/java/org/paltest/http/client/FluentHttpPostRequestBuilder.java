package org.paltest.http.client;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;

public class FluentHttpPostRequestBuilder<T extends FluentHttpPostRequestBuilder> extends FluentHttpWithContentRequestBuilder<T> {
    public static FluentHttpPostRequestBuilder post () {
        return new FluentHttpPostRequestBuilder();
    }
    @Override
    HttpEntityEnclosingRequestBase buildEnclosable(String url) {
        return new HttpPost(url);
    }
}
