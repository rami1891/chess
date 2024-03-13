package ui;

import java.util.Scanner;

import static java.awt.Color.GREEN;
import static ui.EscapeSequences.*;


public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        this.client = new ChessClient(serverUrl);
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



    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length > 0) {
            serverUrl = args[0];
        }
        var repl = new Repl(serverUrl);
        repl.run();
    }

}
