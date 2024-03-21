package ui;

import java.util.Arrays;

import com.google.gson.Gson;
import exception.ResponseException;




import model.*;



public class ChessClient {
    private ChessBoard board;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PreLogin;

    public ChessClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.board = new ChessBoard();


    }


    public String eval(String line) {
        try{
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.PreLogin) {
                return switch (cmd) {
                    case "help" -> help();
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> quit();
                    default -> help();
                };
            }
            else return switch (cmd) {
                case "help" -> help();
                case "logout" -> logout();
                case "listgames" -> listGames(params);
                case "creategame" -> createGame(params);
                case "joingame" -> joinGame(params);
                case "joinobserver" -> joinObserver(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String help() {
        if(state == State.PreLogin){
            return """
                    - Register: register <username> <password> <email>
                    - Login: login <username> <password>
                    - Quit: quit
                    """;
        }
        else{
            return """
                    - Logout: logout
                    - List Games: listgames
                    - Create Game: creategame <gameName>
                    - Join Game: joingame <gameID> <playerColor: Black/White>
                    - Join Observer: joinobserver <gameID>
                    - Quit: quit
                    """;
        }
    }

    public String quit() throws ResponseException {
        System.out.print("Goodbye!");
        System.exit(0);
        return "Goodbye!";
        //return "Goodbye!";
    }

    public String register(String[] params) throws ResponseException {
        try {
            if (params.length != 3) {
                return "Error: Usage: register <username> <password> <email>";
            }
            var username = params[0];
            var password = params[1];
            var email = params[2];
            server.register(username, password, email);
            state = State.PostLogin;
            return "Success: registered";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
        }

    public String login(String[] params) throws ResponseException {
        try {
            if (params.length != 2) {
                return "Error: Usage: login <username> <password>";
            }
            var username = params[0];
            var password = params[1];
            server.login(username, password);
            state = State.PostLogin;
            return "Success: logged in";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String logout() throws ResponseException {
        try {
            server.logout();
            state = State.PreLogin;
            return "Success: logged out";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String createGame(String[] params) throws ResponseException {
        try {
            if (params.length != 1) {
                return "Error: Usage: creategame <gameName>";
            }
            var gameName = params[0];
            server.createGame(gameName);
            return "Success: game created";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String listGames(String[] params) throws ResponseException {
        try {
            var games = server.listGames();
            var result = new StringBuilder();
            var gson = new Gson();
            for (var game : games) {
                result.append(gson.toJson(game)).append("\n");
            }
            return "Success: " + result.toString();
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String joinObserver(String[] params) throws ResponseException {
        try {
            if (params.length != 1) {
                return "Error: bad request";
            }
            var gameIDString = params[0];
            var gameID = Integer.parseInt(gameIDString);
            server.joinObserver(gameID);
            board.setup();

            return "Success: joined observer";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String joinGame(String[] params) throws ResponseException {
        try {
            if (params.length != 2) {
                return "Error: bad request";
            }
            var gameIDString = params[0];
            var gameID = Integer.parseInt(gameIDString);
            var playerColor = params[1];
            playerColor = playerColor.toUpperCase();
            server.joinGame(gameID, playerColor);
            board.setup();
            return "Success: joined game";
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }





}
