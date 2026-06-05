package ui;

import model.GameData;

import java.util.Collection;
import java.util.Scanner;

public class PostLoginUI implements UI{
    @Override
    public String receiveInput() {
        String PRETEXT_MESSAGE =
                EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED_IN] >>> ";
        System.out.print(PRETEXT_MESSAGE);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void displayHelp(){
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "create <GAME NAME>" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - creates a game with the specified name"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "list" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - lists all games"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "join <GAME ID> <WHITE|BLACK>" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - joins the specified game as the specified color"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "observe <GAME ID>" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - observes the specified game"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "logout" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - logs you out of the service"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "quit" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        " - closes the application"

        );
        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_WHITE +
                        "help" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
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
                EscapeSequences.SET_TEXT_COLOR_GREEN + message
        );
    }

    @Override
    public void displayGames(Collection<GameData> games) {
        int count = 1;
        for(GameData game:games){
            System.out.print(
                    EscapeSequences.SET_TEXT_COLOR_GREEN +
                            String.format("%d. Game name: ", count));
            System.out.print(
                    EscapeSequences.SET_TEXT_COLOR_BLUE +
                            game.gameName());
            System.out.print(
                    EscapeSequences.SET_TEXT_COLOR_GREEN +
                            " Game id: ");
            System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_BLUE +
                            String.format("%d",game.gameID()));
            if(game.whiteUsername() != null){
                System.out.println(
                        "\t" +
                        EscapeSequences.SET_TEXT_COLOR_GREEN +
                        "White Player: " +
                        EscapeSequences.SET_TEXT_COLOR_BLUE +
                        game.whiteUsername()
                );
            }
            if(game.blackUsername() != null){
                System.out.println(
                        "\t" +
                                EscapeSequences.SET_TEXT_COLOR_GREEN +
                                "Black Player: " +
                                EscapeSequences.SET_TEXT_COLOR_BLUE +
                                game.blackUsername()
                );
            }
            count++;
        }
    }
}
