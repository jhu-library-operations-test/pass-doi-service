/*
 *
 * Copyright 2019 Johns Hopkins University
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */


import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;


import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class PassDoiServiceIT {

    private static final String doiServiceUrl = "http://localhost:8080/pass-doi-service/doiServlet";

    OkHttpClient client = new OkHttpClient();

    @Before
    public void showEnvironment() {
        System.err.println("PASS FEDORA USER: " + System.getenv("PASS_FEDORA_USER"));
        System.err.println("PASS FEDORA PASSWORD: " + System.getenv("PASS_FEDORA_PASSWORD"));
        System.err.println("PASS FEDORA BASEURL: " + System.getenv("PASS_FEDORA_BASEURL"));
        System.err.println("PASS ELASTICSEARCH URL: " + System.getenv("PASS_ELASTICSEARCH_URL"));
        System.err.println("PASS ELASTICSEARCH LIMIT: " + System.getenv("PASS_ELASTICSEARCH_LIMIT"));
    }

    @Test
    public void smokeTest() throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addQueryParameter("doi", "moo");
        String url = urlBuilder.build().toString();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(okHttpRequest);
        try (Response okHttpResponse = call.execute()) {
            assertEquals(400, okHttpResponse.code());
            assertEquals("{\"error\":\"Supplied DOI is not in valid Crossref format.\"}",
                    okHttpResponse.body().string());
        }
    }

    @Test
    public void noSuchXrefObjectTest() throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addQueryParameter("doi", "10.1234/w.xyz.ABC");
        String url = urlBuilder.build().toString();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(okHttpRequest);
        try (Response okHttpResponse = call.execute()) {
            assertEquals(404, okHttpResponse.code());
            assertEquals("{\"error\":\"The resource for this DOI could not be found on Crossref.\"}",
                    okHttpResponse.body().string());
        }
    }

    @Test
    public void normalBehaviorTest() throws Exception {

        String realDoi = "10.4137/cmc.s38446";
        String id = "";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addQueryParameter("doi", realDoi);
        String url = urlBuilder.build().toString();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(okHttpRequest);
        StringBuffer stringBuffer = new StringBuffer();
        try (Response okHttpResponse = call.execute()) {
            System.err.println(okHttpRequest.toString());
            // assertEquals(200, okHttpResponse.code());
            // jsonReader = Json.createReader(new StringReader(okHttpResponse.body().string()));
            // JsonObject successReport = jsonReader.readObject();
            // JsonObject idObject = successReport.getJsonObject("id");
            // id = idObject.getString("id");
            System.err.println(okHttpResponse.body().string());
        }

        sleep(30000);

        //second time around should give the same id
        urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addQueryParameter("doi", realDoi);
        url = urlBuilder.build().toString();
        okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        call = client.newCall(okHttpRequest);
        stringBuffer = new StringBuffer();
        try (Response okHttpResponse = call.execute()) {
            System.err.println(okHttpRequest.toString());
            //  assertEquals(200, okHttpResponse.code());
            // jsonReader = Json.createReader(new StringReader(okHttpResponse.body().string()));
            // JsonObject successReport = jsonReader.readObject();
            // JsonObject idObject = successReport.getJsonObject("id");
            // assertEquals(id, idObject.get(id));
            System.err.println(okHttpResponse.body().string());
        }
    }


}


