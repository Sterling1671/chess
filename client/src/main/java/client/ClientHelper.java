package client;

import ui.EscapeSequences;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientHelper {
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * @param url the url you want to send data to
     * @param method the http method you want to use
     * @param body the JSON string you want to send, can be null
     * @param authToken the authToken, can be null
     * @return the response of the handler, given as a string
     * @throws InterruptedException if the operation is interrupted
     * @throws IOException  if an I/O error occurs when sending or receiving, or the client has shut down
     */
    public static HttpResponse<String> sendRequest(String url, String method, String body, String authToken)
            throws InterruptedException, IOException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));
        requestBodyBuilder(builder, method, body);
        requestHeaderBuilder(builder, "Authorization", authToken);
        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * @param builder the builder you want to modify
     * @param method the http method you want to use
     * @param body the body you want to check
     */
    private static void requestBodyBuilder(HttpRequest.Builder builder, String method, String body) {
        if (body != null) {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body));
        } else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }
    }

    /**
     * @param builder the builder you want to modify
     * @param header the header you want to modify
     * @param content the content you want to add to the header
     */
    private static void requestHeaderBuilder(HttpRequest.Builder builder, String header, String content){
        if(content != null){
            builder.header(header, content);
        }
    }

    /**
     * @param response Http response object returned from the server
     * @return true if no error occurred, false otherwise
     */
    public static boolean checkResponse(HttpResponse<String> response) {
        var statusCode = response.statusCode();
        switch (statusCode){
            case 200 -> {return true;}
            case 400 -> System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_RED +
                    "Error: That request didn't work, please try again");
            case 401 -> System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_RED +
                    "Error: You're not authorized for that action");
            case 403 -> System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_RED +
                    "Error: That username is already taken");
            default -> System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_RED +
                    "Error: Something went wrong, please try again");
        }
        return false;
    }
}
