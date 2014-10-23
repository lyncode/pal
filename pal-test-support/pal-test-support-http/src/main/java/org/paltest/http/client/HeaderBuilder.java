package org.paltest.http.client;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class HeaderBuilder {
    private final String name;
    private String value;

    public HeaderBuilder(String name) {
        this.name = name;
    }

    public HeaderBuilder withValue (String value) {
        this.value = value;
        return this;
    }

    Header build () {
        return new BasicHeader(name, value);
    }
}
