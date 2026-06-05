package ui;
import model.GameData;

import java.util.Collection;
import java.util.Scanner;

public class PreLoginUI implements UI{

    @Override
    public String receiveInput() {
        String PRETEXT_MESSAGE =
                EscapeSequences.SET_TEXT_COLOR_MAGENTA + "[LOGGED_OUT] >>> ";
        System.out.print(PRETEXT_MESSAGE);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void displayHelp(){
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "register <USERNAME> <PASSWORD> <EMAIL>" +
                        EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                        " - registers you as a user and logs you in"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "login <USERNAME> <PASSWORD>" +
                        EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                        " - logs you in"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "quit" +
                        EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                        " - closes the application"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "help" +
                        EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                        " - displays this menu"

        );
    }

    @Override
    public void displayError(String error) {
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_RED +
                "Error: "+
                error
        );
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_MAGENTA + message
        );
    }

    @Override
    public void displayGames(Collection<GameData> games) {
        int count = 1;
        for(GameData game:games){
            System.out.print(
                    EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                    String.format("%d. Game name: ", count));
            System.out.print(
                    EscapeSequences.SET_TEXT_COLOR_BLUE +
                    game.gameName());
            System.out.print(
                    EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                    " Game id: ");
            System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_BLUE +
                    String.format("%d",game.gameID()));
            count++;
        }
    }
}
