package client;

import chess.ChessGame;
import model.GameData;
import model.requests.*;
import model.results.CreateGameResult;
import model.results.ListGamesResult;
import model.results.LoginResult;
import model.results.RegisterResult;
import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.UI;

import java.util.Arrays;
import java.util.Objects;

public class ChessClient {
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run(){
        UI ui = new PreLoginUI();
        boolean running = true;
        // TODO startup message
        while(running){
            String input = ui.receiveInput();
            if(input.isBlank()) {
                ui.displayError("Please enter a valid input (type help for options)");
                continue;
            }

            // Split the input by word and assign it to an enum
            var inputArray = input.split(" ");
            Options option;
            try{
                option = Options.valueOf(inputArray[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                option = Options.UNKNOWN;
            }

            // Get the rest of the args
            String[] args = Arrays.copyOfRange(inputArray, 1, inputArray.length);

            // Navigate through the options
            if(state == State.SIGNEDOUT) {
                switch (option) {
                    case HELP -> ui.displayHelp();
                    case QUIT -> running = false;
                    case LOGIN -> {
                        if(args.length == 2){
                            LoginRequest request = new LoginRequest(args[0], args[1]);

                            // Attempt to log in the user and transition the UI to logged in
                            try{
                                LoginResult result = server.login(request);
                                authToken = result.authToken();
                                ui.displayMessage("Login successful!");
                                ui = new PostLoginUI();
                                state = State.SIGNEDIN;
                            } catch (ResponseException e) {
                                ui.displayError(e.getMessage());
                            }
                        }

                    }
                    case REGISTER -> {
                        if (args.length == 3) {
                            RegisterRequest request = new RegisterRequest(args[0], args[1], args[2]);

                            // Attempts to register the user and transition the UI to logged in
                            try {
                                RegisterResult result = server.register(request);
                                authToken = result.authToken();
                                ui.displayMessage("Registration successful!");
                                ui = new PostLoginUI();
                                state = State.SIGNEDIN;
                            } catch (ResponseException e) {
                                ui.displayError(e.getMessage());
                            }
                        } else {
                            ui.displayError("Incorrect arguments (Type help for correct formating)");
                        }
                    }
                    case UNKNOWN -> ui.displayError("Unknown Argument");
                }
            }
            else{
                switch(option){
                    case HELP -> ui.displayHelp();
                    case LOGOUT -> {
                        LogoutRequest request = new LogoutRequest(authToken);

                        // Attempt to log out the user and transition the UI to logout
                        try{
                            server.logout(request);
                            authToken = null;
                            ui.displayMessage("Logout successful!");
                            ui = new PreLoginUI();
                            state = State.SIGNEDOUT;
                        } catch (ResponseException e) {
                            ui.displayError(e.getMessage());
                        }
                    }
                    case CREATE -> {
                        if (args.length == 1) {
                            CreateGameRequest request = new CreateGameRequest(authToken, args[0]);

                            // Attempt to create a game
                            try{
                                CreateGameResult result = server.createGame(request);
                                ui.displayMessage("Game created successfully");
                            } catch (ResponseException e) {
                                ui.displayError(e.getMessage());
                            }
                        }
                        else{
                            ui.displayError("Incorrect arguments (Type help for correct formating)");
                        }
                    }
                    case LIST -> {
                        ListGamesRequest request = new ListGamesRequest(authToken);

                        // Attempt to list games
                        try{
                            ListGamesResult result = server.listGames(request);
                            ui.displayGames(result.games());
                        } catch (ResponseException e) {
                            ui.displayError(e.getMessage());
                        }
                    }
                    case JOIN -> {
                        if(args.length == 2) {
                            ChessGame.TeamColor color = Objects.equals(args[1].toUpperCase(), "WHITE") ?
                                ChessGame.TeamColor.WHITE :
                                ChessGame.TeamColor.BLACK;
                            JoinGameRequest request = new JoinGameRequest(
                                    authToken,
                                    color,
                                    Integer.getInteger(args[0]));
                            try {
                                server.joinGame(request);
                            } catch (ResponseException e) {
                                ui.displayError(e.getMessage());
                            }
                        }
                        else{
                            ui.displayError("Incorrect arguments (Type help for correct formating)");
                        }
                    }
                    case OBSERVE -> {
                        if (args.length == 1) {

                            ListGamesRequest request = new ListGamesRequest(authToken);

                            // Attempt to list games
                            try {
                                ListGamesResult result = server.listGames(request);
                                for (GameData data : result.games()) {
                                    if (Integer.getInteger(args[0]) == data.gameID()) {
                                        ui.displayGame(data);
                                    }
                                }

                            }
                            catch (ResponseException e) {
                                ui.displayError(e.getMessage());
                            }
                        }
                    }
                    case UNKNOWN -> ui.displayError("Unknown Argument");

                }
            }
        }
        // TODO exit message
    }
}
