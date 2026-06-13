package ui;

import chess.ChessGame;
import chess.ChessPosition;
import client.WsServerMsgHandler;
import com.google.gson.Gson;
import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
import java.util.Scanner;

public class InGameUI implements UI, WsServerMsgHandler {
    ChessGame.TeamColor teamColor;
    ChessGame chessGame;

    public void setTeamColor(ChessGame.TeamColor color){
        teamColor = color;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }
    public void displayGameBoard(ChessPosition positionToCheck){
        if(ChessGame.TeamColor.BLACK.equals(teamColor)){
            displayGameBoardBlack(chessGame, positionToCheck);
        }
        else{
            displayGameBoardWhite(chessGame, positionToCheck);
        }
    }

    @Override
    public String receiveInput() {
        String pretextMessage =
                EscapeSequences.SET_TEXT_COLOR_GREEN + "[GAMEPLAY] >>> ";
        System.out.print(pretextMessage);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void displayHelp() {
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "redraw" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - redraws the game you are currently in"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "leave" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - leaves the game you are currently in"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "move <START_POSITION> <END_POSITION> <PROMOTION_TYPE>(optional)" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - moves a piece (ex. move a1 a5) or (ex. move b2 b1 queen)"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "resign" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - resigns you from your current game"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "highlight <POSITION>" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - highlghts all the possible moves the piece at that position could make"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "quit" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - closes the application immediately"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "help" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - displays this menu with options for in game commands"

        );
    }

    @Override
    public void displayError(String error) {
        System.out.println(
                "\n" +
                EscapeSequences.SET_TEXT_COLOR_RED +
                        "Error: "+
                        error
        );
        printPrompt();
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(
                "\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + message
        );
        printPrompt();
    }

    @Override
    public void displayGames(Collection<GameData> games) {
        displayError("You shouldn't have been able to get here...");
    }

    @Override
    public void handle(String msgJSON) {
        ServerMessage msg = new Gson().fromJson(msgJSON, ServerMessage.class);
        switch(msg.getServerMessageType()){
            case NOTIFICATION -> {
                NotificationMessage n = new Gson().fromJson(msgJSON, NotificationMessage.class);
                displayMessage(n.getMessage());
            }
            case ERROR -> {
                ErrorMessage e = new Gson().fromJson(msgJSON, ErrorMessage.class);
                displayError(e.getErrorMessage());
            }
            case LOAD_GAME -> {
                LoadGameMessage l = new Gson().fromJson(msgJSON, LoadGameMessage.class);
                displayGameBoard(l.getGame(), null, teamColor);
            }
            default -> displayError(msg.toJson());
        }
    }
    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + "[GAMEPLAY] >>> ");
    }

    @Override
    public void displayGameBoard(ChessGame game, ChessPosition positionToCheck, ChessGame.TeamColor color){
        System.out.print("\n");
        if(ChessGame.TeamColor.BLACK.equals(color)){
            displayGameBoardBlack(game, positionToCheck);
        }
        else{
            displayGameBoardWhite(game, positionToCheck);
        }
        printPrompt();
    }
}
