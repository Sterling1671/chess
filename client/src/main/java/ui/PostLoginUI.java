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
                EscapeSequences.SET_TEXT_COLOR_GREEN +
                        "TODO help"
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
