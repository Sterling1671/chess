package ui;

import client.ServerFacade;

import java.io.IOException;
import java.util.Arrays;
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

    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayMessage(String message) {

    }
}
