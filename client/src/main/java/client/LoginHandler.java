package client;

import model.requests.LoginRequest;
import ui.EscapeSequences;

public class LoginHandler {

    public void handle(String[] args){
        // If there isn't a valid number or type of arguments, return
        if(!checkInput(args)){
            System.out.println(
                    EscapeSequences.SET_TEXT_COLOR_RED +
                    "Error: Please enter the correct number of arguments");
        }
        else{
            LoginRequest request = new LoginRequest(args[0], args[1]);

        }
    }

    private boolean checkInput(String[] args){
        int NUM_ARGS = 2;
        return args.length == NUM_ARGS;
    }
}
