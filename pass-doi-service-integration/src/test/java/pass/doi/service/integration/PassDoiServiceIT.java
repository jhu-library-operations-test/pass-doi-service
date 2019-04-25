package pass.doi.service.integration;



import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.StringReader;


import static org.junit.Assert.assertEquals;

public class PassDoiServiceIT {

    private static final String doiServiceUrl = "http://localhost:8090/doiServlet";

    OkHttpClient client = new OkHttpClient();
    JsonReader jsonReader;

    @Test
    public void smokeTest() throws Exception{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addEncodedQueryParameter("doi", "moo");
        String url = urlBuilder.build().toString();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(okHttpRequest);
        StringBuffer stringBuffer = new StringBuffer();
        try (Response okHttpResponse = call.execute()) {
            assertEquals(400, okHttpResponse.code());
            jsonReader = Json.createReader(new StringReader(okHttpResponse.body().string()));
            JsonObject errorReport = jsonReader.readObject();
            JsonObject errorObject = errorReport.getJsonObject("error");
            assertEquals("Supplied DOI is not in valid Crossref format.", errorObject.getString("error"));
        }
    }

    @Test
    public void noSuchXrefObjectTest() throws Exception{
        HttpUrl.Builder urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addEncodedQueryParameter("doi", "10.1234.xyz.ABC");
        String url = urlBuilder.build().toString();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(okHttpRequest);
        StringBuffer stringBuffer = new StringBuffer();
        try (Response okHttpResponse = call.execute()) {
            assertEquals(404, okHttpResponse.code());
            jsonReader = Json.createReader(new StringReader(okHttpResponse.body().string()));
            JsonObject errorReport = jsonReader.readObject();
            JsonObject errorObject = errorReport.getJsonObject("error");
            assertEquals("The resource for this DOI could not be found on Crossref.", errorObject.getString("error"));
        }
    }

    @Test
    public void normalBehaviorTest() throws Exception{

        String realDoi = "10.4137/cmc.s38446";
        String id = "";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(doiServiceUrl).newBuilder().addEncodedQueryParameter("doi", realDoi);
        String url = urlBuilder.build().toString();
        Request okHttpRequest = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(okHttpRequest);
        StringBuffer stringBuffer = new StringBuffer();
        try (Response okHttpResponse = call.execute()) {
            assertEquals(200, okHttpResponse.code());
            jsonReader = Json.createReader(new StringReader(okHttpResponse.body().string()));
            JsonObject successReport = jsonReader.readObject();
            JsonObject idObject = successReport.getJsonObject("id");
            id = idObject.getString("id");
        }

        //second time around should give the same id
        try (Response okHttpResponse = call.execute()) {
            assertEquals(200, okHttpResponse.code());
            jsonReader = Json.createReader(new StringReader(okHttpResponse.body().string()));
            JsonObject successReport = jsonReader.readObject();
            JsonObject idObject = successReport.getJsonObject("id");
            assertEquals(id, idObject.get(id));
        }
    }


}

