package server.exceptions;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class JsonSyntaxHandler implements ExceptionHandler<JsonSyntaxException> {
    @Override
    public void handle(@NotNull JsonSyntaxException e, @NotNull Context context) {
        context.status(400);
        context.json(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
    }
}
