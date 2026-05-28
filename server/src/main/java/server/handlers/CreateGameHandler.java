package server.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.requests.CreateGameRequest;
import model.results.CreateGameResult;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class CreateGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws BadRequestException, DataAccessException {
        Gson serializer = new Gson();
        String authToken = context.header("Authorization");
        PartialBody body = serializer.fromJson(context.body(), PartialBody.class);
        String gameName = body.gameName;
        if(gameName == null || authToken == null){
            throw new BadRequestException("bad request");
        }

        CreateGameRequest request = new CreateGameRequest(authToken, gameName);
        GameService service = new GameService();
        CreateGameResult result = service.createGame(request);

        context.json(serializer.toJson(result));
    }
    private static class PartialBody {
        public String gameName;
    }
}
