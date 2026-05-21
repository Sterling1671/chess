package server.exceptions;

import com.google.gson.Gson;
import dataaccess.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class UnauthorizedHandler implements ExceptionHandler<UnauthorizedException> {
    @Override
    public void handle(@NotNull UnauthorizedException e, @NotNull Context context) {
        context.status(401);
        context.json(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}
