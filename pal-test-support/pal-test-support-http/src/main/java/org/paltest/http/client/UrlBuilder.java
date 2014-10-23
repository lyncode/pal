package org.paltest.http.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.paltest.exceptions.PalTestException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UrlBuilder {
    public static UrlBuilder path(String path) {
        return new UrlBuilder(path);
    }

    private final String path;
    private List<NameValuePair> parameters = new ArrayList<NameValuePair>();

    UrlBuilder(String path) {
        this.path = path;
    }

    public UrlBuilder withParameter(String name, String value) {
        parameters.add(new BasicNameValuePair(name, value));
        return this;
    }

    String build (String baseUrl) {
        try {
            return new URIBuilder(baseUrl)
                    .setPath(path)
                    .setParameters(parameters)
                    .build()
                    .toString();
        } catch (URISyntaxException e) {
            throw new PalTestException(e);
        }
    }
}
