package ui;

import java.util.Arrays;
import java.util.Collection;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;




import model.*;



public class ChessClient {
    private ChessBoardClient board;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PreLogin;

    private String overPlayerColor;
    private GameData myGame;

    private final NotificationHandler notificationHandler;
    private WebSocketFacade webSocketFacade;


    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.board = new ChessBoardClient();
        this.notificationHandler = notificationHandler;



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
            else if(state == State.PostLogin) return switch (cmd) {
                case "help" -> help();
                case "logout" -> logout();
                case "listgames" -> listGames(params);
                case "creategame" -> createGame(params);
                case "joingame" -> joinGame(params);
                case "joinobserver" -> joinObserver(params);
                case "quit" -> quit();
                default -> help();
            };

            else return switch (cmd){
                case "help" -> help();
                case "leave" -> leaveGame();
                case "redraw" -> redraw();
                case "move" -> move(params);
                case "resign" -> resign();
                case "legalMoves" -> legalMoves(params);
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
        else if (state == State.PostLogin){
            return """
                    - Logout: logout
                    - List Games: listgames
                    - Create Game: creategame <gameName>
                    - Join Game: joingame <gameID> <playerColor: Black/White>
                    - Join Observer: joinobserver <gameID>
                    - Quit: quit
                    """;
        }
        else{
            return """
                    - Help: help
                    - Leave: leave
                    - Redraw: redraw
                    - Move: move <from> <to>
                    - Resign: resign
                    - Legal Moves: legalMoves <piece>
                    
                    """;
        }
    }

    public String quit() throws ResponseException {
        System.out.print("Goodbye!");
        System.exit(0);
        return "Goodbye!";
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
            int index = 0;
            for (var game : games) {
                result.append(index).append(gson.toJson(game)).append("\n");
                index++;
            }
            return "Success: " + "\n" + result.toString();
        } catch (ResponseException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String joinObserver(String[] params) throws ResponseException {
        try {
            if (params.length != 1) {
                return "Error: bad request";
            }
            var gameIdxString = params[0];
            var gameIdx = Integer.parseInt(gameIdxString);

            Collection games = server.listGames();
            if(gameIdx >= games.size() || gameIdx < 0){
                return "Error: bad request";
            }
            int index = 0;
            int gameID = 0;
            for (Object game : games) {
                if (index == gameIdx) {
                    gameID = ((GameData) game).getGameID();
                    myGame = (GameData) game;
                    break;
                }
                index++;
            }

            server.joinObserver(gameID);
            overPlayerColor = "ANY";
            board.setup("ANY", myGame.getGame());
            state = State.GamePlay;

            webSocketFacade = new WebSocketFacade(serverUrl, notificationHandler);
            webSocketFacade.joinObserver(ServerFacade.getAuthToken(), gameID);


            return "Success: joined observer";
        } catch (ResponseException e) {
            myGame = null;
            return "Error: " + e.getMessage();
        }
    }

    public String joinGame(String[] params) throws ResponseException {
        try {
            if (params.length != 2) {
                return "Error: bad request";
            }


            var gameIdxString = params[0];
            var gameIdx = Integer.parseInt(gameIdxString);


            Collection games = server.listGames();
            if(gameIdx >= games.size() || gameIdx < 0){
                return "Error: bad request";
            }


            int index = 0;
            int gameID = 0;
            for (Object game : games) {
                if (index == gameIdx) {
                    gameID = ((GameData) game).getGameID();
                    myGame = (GameData) game;
                    break;
                }
                index++;
            }


            var playerColor = params[1];
            playerColor = playerColor.toUpperCase();
            server.joinGame(gameID, playerColor);
            board.setup(playerColor, myGame.getGame());
            state = State.GamePlay;
            overPlayerColor = playerColor;

            webSocketFacade = new WebSocketFacade(serverUrl, notificationHandler);
            if(playerColor.equals("WHITE")){
                webSocketFacade.joinGame(ServerFacade.getAuthToken(), gameID, ChessGame.TeamColor.WHITE);
            } else{
                webSocketFacade.joinGame(ServerFacade.getAuthToken(), gameID, ChessGame.TeamColor.BLACK);
            }

            return "Success: " + playerColor +" joined game";
        } catch (ResponseException e) {
            myGame = null;
            return "Error: " + e.getMessage();
        }
    }

    public String redraw() {
        if(overPlayerColor.equals("WHITE")){
            board.setup(overPlayerColor, myGame.getGame());

        } else if(overPlayerColor.equals("BLACK")){
            board.setup(overPlayerColor, myGame.getGame());

        }
        else{
            board.setup("ANY", myGame.getGame());
        }
        return "Success: redrawn";
    }

    public String leaveGame() throws ResponseException {

        webSocketFacade.leaveGame(ServerFacade.getAuthToken(), myGame.getGameID());
        state = State.PostLogin;
        overPlayerColor = null;



        return "Success: left game";
    }


    public String move(String[] params) throws ResponseException {
        return "Success: moved";
    }


    public String resign() throws ResponseException {
        webSocketFacade.resignGame(ServerFacade.getAuthToken(), myGame.getGameID());
        state = State.PostLogin;
        overPlayerColor = null;

        return "Success: resigned";
    }

    public String legalMoves(String[] params) throws ResponseException {
        return "Success: legal moves";
    }





}
