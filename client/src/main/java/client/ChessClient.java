package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import model.requests.*;
import model.results.CreateGameResult;
import model.results.ListGamesResult;
import model.results.LoginResult;
import model.results.RegisterResult;
import ui.InGameUI;
import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.UI;
import websocket.commands.UserGameCommand;

import java.util.Arrays;
import java.util.Objects;

public class ChessClient {
    private String authToken = null;
    private int myGameId = 0;
    private final ServerFacade server;
    private final WebSocketFacade wsServer;
    private State state = State.SIGNEDOUT;
    private final InGameUI myInGameUI;
    UI ui;
    boolean running;

    public ChessClient(String serverUrl){
        myInGameUI = new InGameUI();
        server = new ServerFacade(serverUrl);
        wsServer = new WebSocketFacade(serverUrl,myInGameUI);
        ui = new PreLoginUI();
    }

    public void run(){
        running = true;
        ui.displayMessage("Welcome to the CS240 Chess Game!\nType help to start");
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
            navigateMenu(option, args);
        }
        ui.displayMessage("Thanks for playing!");
    }

    private void navigateMenu(Options option, String[] args) {
        if(state == State.SIGNEDOUT) {
            stateSingedOut(option, args);
        }
        else if(state == State.SIGNEDIN){
            stateSingedIn(option, args);
        }
        else{
            stateInGame(option, args);
        }
    }

    private void stateSingedIn(Options option, String[] args) {
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
                        ui.displayMessage(String.format("Game created successfully (ID: %d)", result.gameID()));
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
                    joinGame(args);
                }
                else{
                    ui.displayError("Incorrect arguments (Type help for correct formating)");
                }
            }
            case OBSERVE -> {
                if (args.length == 1) {

                    int gameID;
                    try{
                        gameID = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        ui.displayError("Incorrect arguments (Type help for correct formating)");
                        break;
                    }
                    // Attempt to list games
                    try{
                        ListGamesRequest requestGame = new ListGamesRequest(authToken);
                        GameData game = server.getGame(requestGame, gameID);
                        myGameId = gameID;
                        ui = myInGameUI;
                        myInGameUI.setTeamColor(null);
                        myInGameUI.setChessGame(game.game());
                        myInGameUI.displayGameBoard(null);
                        wsServer.sendCommand(authToken, myGameId, UserGameCommand.CommandType.CONNECT);

                        state = State.INGAME;
                    }
                    catch(ResponseException e){
                        ui.displayError(e.getMessage());
                    }
                }
                else{
                    ui.displayError("Incorrect arguments (Type help for correct formating)");
                }
            }
            case QUIT -> running = false;
            default -> ui.displayError("Unknown Argument");

        }
    }

    private void joinGame(String[] args) {
        int gameID;
        try{
            gameID = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            ui.displayError("Please enter a valid Game ID");
            return;
        }
        ChessGame.TeamColor color;
        if(Objects.equals(args[1].toUpperCase(), "WHITE")){
            color = ChessGame.TeamColor.WHITE;
        }
        else if(Objects.equals(args[1].toUpperCase(), "BLACK")){
            color = ChessGame.TeamColor.BLACK;
        }
        else{
            ui.displayError("Please enter a valid color;");
            return;
        }

        JoinGameRequest request = new JoinGameRequest(
                authToken,
                color,
                gameID);
        try {
            server.joinGame(request);
            ui.displayMessage("Game joined successfully");
        } catch (ResponseException e) {
            ui.displayError(e.getMessage());
            return;
        }
        GameData game;
        try{
            ListGamesRequest requestGame = new ListGamesRequest(authToken);
            game = server.getGame(requestGame, gameID);
        }
        catch(ResponseException e){
            ui.displayError(e.getMessage());
            return;
        }
        myGameId = gameID;
        ui = myInGameUI;
        myInGameUI.setTeamColor(color);
        myInGameUI.setChessGame(game.game());
        myInGameUI.displayGameBoard(null);
        wsServer.sendCommand(authToken, myGameId, UserGameCommand.CommandType.CONNECT);
        state = State.INGAME;
    }

    private void stateSingedOut(Options option, String[] args) {
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
                else{
                    ui.displayError("Incorrect arguments (Type help for correct formating)");
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
            default -> ui.displayError("Unknown Argument");
        }
    }

    private void stateInGame(Options option, String[] args){
        switch(option){
            case REDRAW -> myInGameUI.displayGameBoard(null);
            case LEAVE -> {
                myInGameUI.setChessGame(null);
                myInGameUI.setTeamColor(null);
                wsServer.sendCommand(authToken, myGameId, UserGameCommand.CommandType.LEAVE);
                myGameId = 0;
                ui = new PostLoginUI();
                state = State.SIGNEDIN;
            }
            case MOVE -> {
                if(args.length == 2){
                    ChessMove move;
                    try {
                        ChessPosition startPosition = ChessPosition.fromString(args[0]);
                        ChessPosition endPosition = ChessPosition.fromString(args[1]);
                        move = new ChessMove(startPosition, endPosition, null);
                    } catch (IllegalArgumentException e) {
                        myInGameUI.displayError("Invalid move, try again");
                        break;
                    }
                    try{
                        wsServer.makeMove(authToken, myGameId, move);
                    } catch (ResponseException e) {
                        myInGameUI.displayError(e.getMessage());

                    }
                }
                else if(args.length == 3){
                    ChessMove move;
                    try {
                        ChessPosition startPosition = ChessPosition.fromString(args[0]);
                        ChessPosition endPosition = ChessPosition.fromString(args[1]);

                        move = new ChessMove(
                                startPosition,
                                endPosition,
                                ChessPiece.PieceType.valueOf(args[2].toUpperCase())
                        );
                    } catch (IllegalArgumentException e) {
                        myInGameUI.displayError("Invalid move, try again");
                        break;
                    }
                    try{
                        wsServer.makeMove(authToken, myGameId, move);
                    } catch (ResponseException e) {
                        myInGameUI.displayError(e.getMessage());

                    }
                }
                else{
                    myInGameUI.displayError("Please enter the correct number of arguments");
                }
            }
            case RESIGN -> wsServer.sendCommand(authToken, myGameId, UserGameCommand.CommandType.RESIGN);
            case HIGHLIGHT -> {
                if(args.length == 1){
                    ChessPosition positionToCheck;
                    try {
                        positionToCheck = ChessPosition.fromString(args[0]);
                    } catch (IllegalArgumentException e) {
                        myInGameUI.displayError("Please enter a valid chess position");
                        break;
                    }
                    myInGameUI.displayGameBoard(positionToCheck);
                }
            }
            case HELP -> myInGameUI.displayHelp();
            default -> myInGameUI.displayError("Please enter a valid option");
        }
    }
}
