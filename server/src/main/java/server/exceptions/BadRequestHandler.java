package server.exceptions;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BadRequestHandler implements ExceptionHandler<BadRequestException>{
    @Override
    public void handle(@NotNull BadRequestException e, @NotNull Context context) {
        context.status(400);
        context.json(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}
