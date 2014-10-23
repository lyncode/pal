package org.paltest.http.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.paltest.exceptions.PalTestException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class FluentHttpResponse {
    private final byte[] content;
    private HttpResponse response;
    private URL uri;

    public FluentHttpResponse(HttpResponse response, URL uri) {
        this.response = response;
        this.uri = uri;
        this.content = retrieveContent(response);
    }

    private byte[] retrieveContent(HttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null)
                return IOUtils.toByteArray(entity.getContent());
            else
                return null;
        } catch (Exception e) {
            throw new PalTestException(e);
        }
    }

    public String contentAsString() {
        if (content != null) return new String(content);
        else return null;
    }

    public InputStream content () {
        return new ByteArrayInputStream(content);
    }

    public int statusCode() {
        return response.getStatusLine().getStatusCode();
    }

    public List<Header> headers() {
        return Arrays.asList(response.getAllHeaders());
    }

    public URL uri() {
        return uri;
    }
}
