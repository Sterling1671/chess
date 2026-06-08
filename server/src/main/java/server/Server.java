package server;

import com.google.gson.JsonSyntaxException;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import io.javalin.*;
import server.exceptions.*;
import server.handlers.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        createHandlers(javalin);
        createExceptions(javalin);

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void createHandlers(Javalin javalinServer){

        javalinServer.post("/user", new RegistrationHandler());
        javalinServer.post("/session", new LoginHandler());
        javalinServer.delete("/session", new LogoutHandler());
        javalinServer.get("/game", new ListGamesHandler());
        javalinServer.post("/game", new CreateGameHandler());
        javalinServer.put("/game", new JoinGameHandler());
        javalinServer.delete("/db",new ClearGameHandler());
        javalinServer.ws("/ws",new WsRequestHandler());
    }
    private void createExceptions(Javalin javalinServer){
        javalinServer.exception(JsonSyntaxException.class, new JsonSyntaxHandler());
        javalinServer.exception(UnauthorizedException.class, new UnauthorizedHandler());
        javalinServer.exception(AlreadyTakenException.class, new AlreadyTakenHandler());
        javalinServer.exception(BadRequestException.class, new BadRequestHandler());
        javalinServer.exception(Exception.class, new GeneralHandler());
    }
}
