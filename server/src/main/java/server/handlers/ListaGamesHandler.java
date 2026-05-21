package server.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.requests.ListGamesRequest;
import model.results.ListGamesResult;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class ListaGamesHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        Gson serializer = new Gson();
        String authToken = context.header("Authorization");
        if(authToken == null){
            throw new BadRequestException("bad request");
        }
        ListGamesRequest request = new ListGamesRequest(authToken);
        GameService service = new GameService();
        ListGamesResult result = service.listGames(request);

        context.json(serializer.toJson(result));
    }
}
