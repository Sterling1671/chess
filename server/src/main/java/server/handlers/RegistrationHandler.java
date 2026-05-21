package server.handlers;

import com.google.gson.Gson;
import dataaccess.BadRequestException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.requests.RegisterRequest;
import model.results.RegisterResult;
import org.jetbrains.annotations.NotNull;
import service.UserService;

public class RegistrationHandler implements Handler {

    @Override
    public void handle(@NotNull Context context) throws BadRequestException {
        Gson serializer = new Gson();
        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);
        if(request.username() == null || request.password() == null || request.email() == null){
            throw new BadRequestException("bad request");
        }
        UserService service = new UserService();
        RegisterResult result = service.register(request);

        context.json(serializer.toJson(result));
    }
}
