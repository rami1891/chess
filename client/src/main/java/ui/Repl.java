package ui;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;


public class Repl implements NotificationHandler{
    private final ChessClient client;

    public Repl(String serverUrl) {
        this.client = new ChessClient(serverUrl, this);
    }




    public void run(){
        System.out.println("Welcome to Chess!");
        System.out.print(client.help());




        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }


    @Override
    public void notify(String notification) {
        ServerMessage message = new Notification(notification);
        switch(message.getServerMessageType()) {
            case NOTIFICATION -> {
               System.out.println();
               Notification notif = new Gson().fromJson(notification, Notification.class);
                System.out.println(notif.getMessage());

            }
            case LOAD_GAME -> {
                System.out.println();
                LoadGameMessage loadGameMessage = new Gson().fromJson(notification, LoadGameMessage.class);
                System.out.println(loadGameMessage.getGame().getBoard());

            }

            case ERROR -> {
                System.out.println();
                Error error = new Gson().fromJson(notification, Error.class);
                System.out.println(SET_TEXT_COLOR_RED + error.getMessage());
            }
        }
    }
}
