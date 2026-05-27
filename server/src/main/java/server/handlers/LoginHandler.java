package server.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.requests.LoginRequest;
import model.results.LoginResult;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class LoginHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws BadRequestException, DataAccessException {
        Gson serializer = new Gson();
        LoginRequest request = serializer.fromJson(context.body(), LoginRequest.class);
        if(request.username() == null || request.password() == null){
            throw new BadRequestException("bad request");
        }
        UserService service = new UserService();
        LoginResult result = service.login(request);
        context.json(serializer.toJson(result));
    }
}
