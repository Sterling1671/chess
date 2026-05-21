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
        String authToken = context.header("Authorization");
        if(authToken == null){
            throw new BadRequestException("bad request");
        }
        LogoutRequest request = new LogoutRequest(authToken);

        UserService service = new UserService();
        service.logout(request);
        context.json(serializer.toJson(Map.of()));
    }
}
