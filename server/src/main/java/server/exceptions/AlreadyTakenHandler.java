package server.exceptions;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class AlreadyTakenHandler implements ExceptionHandler<AlreadyTakenException> {
    @Override
    public void handle(@NotNull AlreadyTakenException e, @NotNull Context context) {
        context.status(403);
        context.json(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}
