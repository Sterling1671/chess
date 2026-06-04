package ui;

import model.GameData;

import java.util.Collection;

public interface UI {
    String receiveInput();
    void displayHelp();
    void displayError(String error);
    void displayMessage(String message);
    void displayGames(Collection<GameData> games);
    void observeGame(GameData game);

}
