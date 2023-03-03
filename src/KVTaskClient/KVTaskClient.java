package KVTaskClient;


import Manager.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String apiToken;

    HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(URL url1) {
        try {
            URI url = URI.create(String.valueOf(url1));
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            this.apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public KVTaskClient(URL url1, String token) {
        URI url = URI.create(String.valueOf(url1));
        this.apiToken = token;
    }

    public void put(String key, String json) {
        try {
            URI url = URI.create(Managers.getLocalHostKvServer() + "save/" + key + "?API_TOKEN=" + apiToken);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        try {
            URI url1 = URI.create(Managers.getLocalHostKvServer() + "load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
