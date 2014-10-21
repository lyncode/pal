package org.paltest.pal.example;

import org.junit.Test;
import org.paltest.pal.PalTesty;
import org.testy.http.TestyHttpClient;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.paltest.pal.syntax.SyntacticSugar.then;
import static org.testy.TestyHttp.*;

public class TestyExample extends PalTesty {
    private TestyHttpClient google = server("Joao", "Google", "http://www.google.com");
    private TestyHttpClient yahoo = server("Joao", "Yahoo", "http://www.yahoo.com");

    @Test
    public void requestSentAndResponseReceived() throws Exception {
        when(google.receives(get().to(path("/one"))));
        then(google.sends(), response().withStatusCode(equalTo(404)));

        when(yahoo.receives(get().to(path("/"))));
        then(yahoo.sends(), response().withStatusCode(equalTo(200)));
    }
}
