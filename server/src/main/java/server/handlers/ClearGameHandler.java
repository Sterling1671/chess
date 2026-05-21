package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.Map;

public class ClearGameHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        UserService userService = new UserService();
        GameService gameService = new GameService();
        AuthService authService = new AuthService();

        userService.clear();
        gameService.clear();
        authService.clear();

        context.json(new Gson().toJson(Map.of()));
    }
}
