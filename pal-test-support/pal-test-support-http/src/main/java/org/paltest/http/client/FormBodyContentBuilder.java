package org.paltest.http.client;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.paltest.exceptions.PalTestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FormBodyContentBuilder extends BodyContentBuilder {
    public static FormBodyContentBuilder form () {
        return new FormBodyContentBuilder();
    }

    private List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();

    public FormValuePairBuilder withParameter(String name) {
        return new FormValuePairBuilder(name, this);
    }
    public FormBodyContentBuilder and() {
        return this;
    }

    @Override
    byte[] build() {
        try {
            return IOUtils.toByteArray(new UrlEncodedFormEntity(valuePairs).getContent());
        } catch (IOException e) {
            throw new PalTestException(e);
        }
    }

    public static class FormValuePairBuilder {
        private final String name;
        private final FormBodyContentBuilder builder;

        public FormValuePairBuilder(String name, FormBodyContentBuilder builder) {
            this.name = name;
            this.builder = builder;
        }

        public FormBodyContentBuilder withValue (String value) {
            builder.valuePairs.add(new BasicNameValuePair(name, value));
            return builder;
        }
    }
}
