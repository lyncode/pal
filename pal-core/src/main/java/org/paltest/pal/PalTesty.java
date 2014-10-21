/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.paltest.pal;

import org.paltest.pal.syntax.flow.Communication;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.testy.TestyHttp;
import org.testy.http.TestyHttpClient;

public abstract class PalTesty extends PalTest {
    protected TestyHttpClient server (final String originName, final String destinationName, final String baseUrl) {
        TestyHttpClient server = TestyHttp.server(baseUrl);
        server.interceptor(new TestyHttpClient.RequestInterceptor() {
            @Override
            public void intercept(HttpRequestBase request) {
                String replace = request.getURI().toString().replace(baseUrl, "");
                communications().add(new Communication(originName, destinationName, replace, requestToString(request)));
            }
        });

        server.interceptor(new TestyHttpClient.ResponseInterceptor() {
            @Override
            public void intercept(HttpResponse response) {
                communications().add(new Communication(destinationName, originName, String.valueOf(response.getStatusLine().getStatusCode()), responseToString(response)));
            }
        });

        return server;
    }

    protected String responseToString(HttpResponse response) {
        return response.toString();
    }

    protected String requestToString(HttpRequestBase request) {
        return request.toString();
    }
}
