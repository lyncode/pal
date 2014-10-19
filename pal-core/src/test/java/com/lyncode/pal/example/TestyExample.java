package com.lyncode.pal.example;

import com.lyncode.pal.PalTesty;
import com.lyncode.testy.http.TestyHttpClient;
import org.junit.Test;

import static com.lyncode.pal.syntax.SyntacticSugar.then;
import static com.lyncode.testy.TestyHttp.get;
import static com.lyncode.testy.TestyHttp.response;
import static com.lyncode.testy.http.UrlBuilder.path;
import static org.hamcrest.core.IsEqual.equalTo;

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
