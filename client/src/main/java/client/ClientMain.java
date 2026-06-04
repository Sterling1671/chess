package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080/";
        ChessClient client = new ChessClient(serverURL);
        client.run();
    }
}
