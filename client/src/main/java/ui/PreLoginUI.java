package ui;

import client.ServerFacade;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class PreLoginUI implements UI{

    public void displayUI() throws IOException, InterruptedException {
        boolean running = true;
        while(running){
            String input = getInput();

            // Checks if input is blank
            if(input.isBlank()){
                System.out.println(
                        EscapeSequences.SET_TEXT_COLOR_RED +
                        "Error: Please enter an option (type Help for options)");
                continue;
            }

            // Checks if input is valid, unknown otherwise
            var inputArray = input.split(" ");
            Options option;
            try{
                option = Options.valueOf(inputArray[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                option = Options.UNKNOWN;
            }

            // Get the rest of the arguments
            String[] args = Arrays.copyOfRange(inputArray, 1, inputArray.length);

            // Navigate through the options
            switch (option){
                case HELP -> displayHelp();
                case QUIT -> running = false;
                case LOGIN -> ServerFacade.login(args);
                case REGISTER -> ServerFacade.register(args);
                case UNKNOWN -> System.out.println(
                                    EscapeSequences.SET_TEXT_COLOR_RED +
                                    "Error: Please enter a valid option (type Help for options)");
            }
        }
    }

    public void displayHelp(){

    }

    public String getInput(){
        String PRETEXT_MESSAGE =
                EscapeSequences.SET_TEXT_COLOR_MAGENTA + "[LOGGED_OUT] >>> ";
        System.out.print(PRETEXT_MESSAGE);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
