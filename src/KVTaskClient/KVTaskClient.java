package KVTaskClient;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String apiToken;


    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(URL url1) throws IOException, InterruptedException {
        URI url = URI.create(String.valueOf(url1));
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        this.apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI url1 = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        return response1.body();
    }
}
