package org.paltest.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.Matcher;
import org.paltest.exceptions.PalTestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class FluentHttpClient {
    private final HttpClient client;
    private final String baseURL;
    private final List<FluentHttpResponse> responses = new ArrayList<FluentHttpResponse>();
    private final List<RequestInterceptor> requestInterceptors = new ArrayList<RequestInterceptor>();
    private final List<ResponseInterceptor> responseInterceptors = new ArrayList<ResponseInterceptor>();

    public FluentHttpClient(String baseURL) {
        this(HttpClientBuilder.create().build(), baseURL);
    }

    public FluentHttpClient(HttpClient client, String baseURL) {
        this.client = client;
        this.baseURL = baseURL;
    }

    public FluentHttpClient receives (FluentHttpRequestBuilder request) {
        try {
            HttpRequestBase build = request.request(baseURL);
            for (RequestInterceptor requestInterceptor : requestInterceptors) {
                requestInterceptor.intercept(build);
            }
            HttpResponse response = client.execute(build);
            for (ResponseInterceptor responseInterceptor : responseInterceptors) {
                responseInterceptor.intercept(response);
            }
            responses.add(new FluentHttpResponse(response, build.getURI().toURL()));
        } catch (IOException e) {
            throw new PalTestException(e);
        }
        return this;
    }

    public FluentHttpClient received (FluentHttpRequestBuilder request) {
        try {
            HttpRequestBase build = request.request(baseURL);
            client.execute(build);
        } catch (IOException e) {
            throw new PalTestException(e);
        }
        return this;
    }

    public FluentHttpResponse sends () {
        if (responses.isEmpty()) return null;
        else return responses.get(responses.size() - 1);
    }

    public FluentHttpClient interceptor (RequestInterceptor interceptor) {
        this.requestInterceptors.add(interceptor);
        return this;
    }

    public FluentHttpClient interceptor (ResponseInterceptor interceptor) {
        this.responseInterceptors.add(interceptor);
        return this;
    }

    public boolean sends (Matcher<? super HttpResponse> matcher) {
        return hasItem(matcher).matches(responses);
    }

    public interface RequestInterceptor {
        void intercept(HttpRequestBase request);
    }
    public interface ResponseInterceptor {
        void intercept(HttpResponse response);
    }
}
