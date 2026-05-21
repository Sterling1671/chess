package server.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.requests.LogoutRequest;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.util.Map;

public class LogoutHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws BadRequestException {
        Gson serializer = new Gson();
        LogoutRequest request = serializer.fromJson(context.body(), LogoutRequest.class);
        if(request.authToken() == null){
            throw new BadRequestException("bad request");
        }
        UserService service = new UserService();
        service.logout(request);
        context.json(Map.of());
    }
}
