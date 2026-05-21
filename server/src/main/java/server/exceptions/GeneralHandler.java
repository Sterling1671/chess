package server.exceptions;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GeneralHandler implements ExceptionHandler<Exception> {
    @Override
    public void handle(@NotNull Exception e, @NotNull Context context) {
        context.status(500);
        context.json(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}
