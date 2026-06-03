package ui;

public interface UI {
    public String receiveInput();
    public void displayHelp();
    public void displayError(String error);
    public void displayMessage(String message);
}
