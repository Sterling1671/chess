package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.requests.*;
import model.results.CreateGameResult;
import model.results.ListGamesResult;
import model.results.LoginResult;
import model.results.RegisterResult;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final String serverURL;
    private static final HttpClient client = HttpClient.newHttpClient();


    public ServerFacade(String url){
        serverURL = url;
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        String body = new Gson().toJson(request);
        HttpResponse<String> response = sendRequest(serverURL + "session", "POST", body, null);
        checkResponse(response);
        return new Gson().fromJson(response.body(), LoginResult.class);
    }
    public RegisterResult register(RegisterRequest request) throws ResponseException{
        String body = new Gson().toJson(request);
        HttpResponse<String> response = sendRequest(serverURL + "user", "POST", body, null);
        checkResponse(response);
        return new Gson().fromJson(response.body(), RegisterResult.class);
    }
    public void logout(LogoutRequest request) throws ResponseException{
        HttpResponse<String> response = sendRequest(serverURL + "session", "DELETE", null, request.authToken());
        checkResponse(response);
    }
    public CreateGameResult createGame(CreateGameRequest request) throws ResponseException{
        String body = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(request);
        HttpResponse<String> response = sendRequest(serverURL + "game", "POST", body, request.authToken());
        checkResponse(response);
        return new Gson().fromJson(response.body(), CreateGameResult.class);
    }
    public ListGamesResult listGames(ListGamesRequest request) throws ResponseException{
        HttpResponse<String> response = sendRequest(serverURL + "game", "GET", null, request.authToken());
        checkResponse(response);
        return new Gson().fromJson(response.body(), ListGamesResult.class);
    }
    public void joinGame(JoinGameRequest request) throws ResponseException{
        String body = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(request);
        HttpResponse<String> response = sendRequest(serverURL + "game", "PUT", body, request.authToken());
        checkResponse(response);
    }



    /**
     * @param url the url you want to send data to
     * @param method the http method you want to use
     * @param body the JSON string you want to send, can be null
     * @param authToken the authToken, can be null
     * @return the response of the handler, given as a string
     * @throws ResponseException if the operation isn't successful
     */
    private static HttpResponse<String> sendRequest(String url, String method, String body, String authToken)
            throws ResponseException{
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url));
        requestBodyBuilder(builder, method, body);
        requestHeaderBuilder(builder, "Authorization", authToken);
        HttpRequest request = builder.build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch(Exception e) {
            throw new ResponseException("Something went wrong, try again");
        }
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
     */
    private static void checkResponse(HttpResponse<String> response) throws ResponseException{
        var statusCode = response.statusCode();
        switch (statusCode){
            case 200 -> {}
            case 400 -> throw new ResponseException("That request didn't work, please try again");
            case 401 -> throw new ResponseException("You're not authorized for that action");
            case 403 -> throw new ResponseException("That username is already taken");
            default ->  throw new ResponseException("Something went wrong, please try again");
        }
    }
}
