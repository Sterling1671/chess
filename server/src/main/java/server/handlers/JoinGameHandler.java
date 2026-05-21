package server.handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.requests.JoinGameRequest;
import org.jetbrains.annotations.NotNull;
import service.GameService;

import java.util.Map;
import java.util.Objects;

public class JoinGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        Gson serializer = new Gson();
        String authToken = context.header("Authorization");
        PartialBody body = serializer.fromJson(context.body(), PartialBody.class);
        if(authToken == null || body.playerColor == null){
            throw new BadRequestException("bad request");
        }
        ChessGame.TeamColor color;
        if(Objects.equals(body.playerColor, "WHITE")){
            color = ChessGame.TeamColor.WHITE;
        }
        else if(Objects.equals(body.playerColor, "BLACK")){
            color = ChessGame.TeamColor.BLACK;
        }
        else{
            throw new BadRequestException("bad request");
        }


        JoinGameRequest request = new JoinGameRequest(authToken, color, body.gameID);
        GameService service = new GameService();
        service.joinGame(request);
        context.json(serializer.toJson(Map.of()));

    }
    private static class PartialBody {
        public String playerColor;
        public int gameID;
    }
}

