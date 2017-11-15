package fr.rbs.http.client;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Http2Client {

    public Http2Client() {
    }

    public static void main(String[] args) throws Exception {

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://romain:8181/test/http2"))
                    .GET()
                    .build();
            //String body handler
            HttpResponse<String> strResponse = client.send(request, HttpResponse.BodyHandler.asString());
            System.out.println(strResponse.statusCode());
            SSLParameters sslParameters = strResponse.sslParameters();
            System.out.println("Maximum packet size : "+sslParameters.getMaximumPacketSize());
            //System.out.println(response.body());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        System.out.println("SYNC CALL");
        callSyncHttp();
        System.out.println("END");

        System.out.println("ASYNC CALL");
        callASyncHttp();
        System.out.println("END");*/

    }

    public static void callSyncHttp() {
        try {
            SSLContext  sslcontext = SSLContext.getDefault();

            HttpClient httpClient = HttpClient.newHttpClient(); //Create a HttpClient
            System.out.println(httpClient.version());
            //HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("https://www.google.com/")).GET().build(); //Create a GET request for the given URI
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("https://localhost:8181/test/http2")).GET().build(); //Create a GET request for the given URI
            Map<String, List<String>> headers = httpRequest.headers().map();
            headers.forEach((k, v) -> System.out.println(k + "-" + v));
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandler.asString());

            System.out.println("Status code : " + httpResponse.statusCode());
            System.out.println("body : " + httpResponse.body());
        } catch (Exception e) {
            System.out.println("message " + e);
        }
    }

    public static void callASyncHttp() throws InterruptedException {

        try {
            HttpClient httpClient = HttpClient.newHttpClient(); //Create a HttpClient
            System.out.println(httpClient.version());
            //HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("https://www.google.com/")).GET().build(); //Create a GET request for the given URI
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI("https://localhost:8181/test/http2")).GET().build(); //Create a GET request for the given URI
            Map<String, List<String>> headers = httpRequest.headers().map();
            headers.forEach((k, v) -> System.out.println(k + "-" + v));


            CompletableFuture<HttpResponse<String>> httpResponse = httpClient.sendAsync(httpRequest, HttpResponse.BodyHandler.asString());

            httpResponse.whenComplete((t, k) -> {
                System.out.println("Status is :" + t.statusCode());
                System.out.println("body is :" + t.body());
            });


            httpResponse.get();

        } catch (
                Exception e)

        {
            System.out.println("message " + e);
        }
    }
}
