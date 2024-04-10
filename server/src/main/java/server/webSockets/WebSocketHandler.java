package server.webSockets;


import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDao;
    private AuthDAO authDao;
    private UserDAO userDao;


    public WebSocketHandler(GameDAO gameDao, AuthDAO authDao, UserDAO userDao) {
        this.gameDao = gameDao;
        this.authDao = authDao;
        this.userDao = userDao;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayerCmd cmd = new Gson().fromJson(message, JoinPlayerCmd.class);
                joinGame(cmd, session);
                break;
            case JOIN_OBSERVER:
                JoinObserverCmd cmd2 = new Gson().fromJson(message, JoinObserverCmd.class);
                joinObserver(cmd2, session);
                break;
            case MAKE_MOVE:
                MakeMove command1 = new Gson().fromJson(message, MakeMove.class);
                makeMove(command1, session);
                break;
            case LEAVE:
                LeaveCmd cmd3 = new Gson().fromJson(message, LeaveCmd.class);
                leaveGame(cmd3, session);
                break;
            case RESIGN:
                ResignCmd cmd4 = new Gson().fromJson(message, ResignCmd.class);
                resignGame(cmd4, session);
                break;

        }
    }


    private void joinGame(JoinPlayerCmd command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try{
            if(authDao.findAuth(command.getAuthString()) && (gameDao.getGame(command.getGameID()) != null)){
                GameData game = gameDao.getGame(command.getGameID());

                if(command.getPlayerColor() == null){
                    Error error = new Error("Player color not specified");
                    session.getRemote().sendString(new Gson().toJson(error));
                }
                else if("WHITE".equals(command.getPlayerColor().toString())){
                    if((game.getWhiteUsername() == null) || !(authDao.getAuth(command.getAuthString()).getUsername().equals(game.getWhiteUsername()))){
                        Error error = new Error("Player already joined");
                        session.getRemote().sendString(new Gson().toJson(error));
                    }
                    else {
                        game.setWhiteUsername(command.getAuthString());
                        LoadGameMessage message = new LoadGameMessage(game.getGame());
                        session.getRemote().sendString(new Gson().toJson(message));
                        Notification notification = new Notification("Player joined game");
                        connections.broadcast(command.getAuthString(), command.getGameID(), notification);
                    }
                }
                else{
                    if((game.getBlackUsername() == null) || !(authDao.getAuth(command.getAuthString()).getUsername().equals(game.getBlackUsername()))){
                        Error error = new Error("Player already joined");
                        session.getRemote().sendString(new Gson().toJson(error));
                    }
                    else{
                    game.setBlackUsername(command.getAuthString());
                    LoadGameMessage message = new LoadGameMessage(game.getGame());
                    session.getRemote().sendString(new Gson().toJson(message));
                    Notification notification = new Notification("Player joined game");
                    connections.broadcast(command.getAuthString(), command.getGameID(), notification);}

                }

            }
            else{
                Error error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataErrorException | DataAccessException  | IOException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }

    }

    private void joinObserver(JoinObserverCmd command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try{
            if(authDao.findAuth(command.getAuthString()) && (gameDao.getGame(command.getGameID()) != null)){
                GameData game = gameDao.getGame(command.getGameID());
                game.addObservers(authDao.getAuth(command.getAuthString()).getUsername());
                LoadGameMessage message = new LoadGameMessage(game.getGame());
                session.getRemote().sendString(new Gson().toJson(message));
                Notification notification = new Notification("Observer joined game");
                connections.broadcast(command.getAuthString(), command.getGameID(), notification);

            }
            else{
                Error error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataErrorException | DataAccessException e ) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }

    }


    private void makeMove(MakeMove command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        ChessMove move = command.getMove();


        try {
            if (authDao.findAuth(command.getAuthString())) {

                GameData game = gameDao.getGame(command.getGameID());
                if(game.isGameOver()){
                    Error error = new Error("Game already over");
                    session.getRemote().sendString(new Gson().toJson(error));
                }
                else if(Objects.equals(game.getGame().getTeamTurn().toString(), "WHITE") && !(Objects.equals(game.getWhiteUsername(), authDao.getAuth(command.getAuthString()).getUsername())))
                    session.getRemote().sendString(new Gson().toJson(new Error("Not your turn")));
                else if(Objects.equals(game.getGame().getTeamTurn().toString(), "BLACK") && !(Objects.equals(game.getBlackUsername(), authDao.getAuth(command.getAuthString()).getUsername())))
                    session.getRemote().sendString(new Gson().toJson(new Error("Not your turn")));
                else
                if ((game.getWhiteUsername().equals(authDao.getAuth(command.getAuthString()).getUsername())) || (game.getBlackUsername().equals(authDao.getAuth(command.getAuthString()).getUsername()))) {
                    game.getGame().makeMove(move);

                    gameDao.gameOverride(game);
                    LoadGameMessage message = new LoadGameMessage(game.getGame());
                    connections.broadcast("", command.getGameID(), message);
                    if (game.isGameOver()) {
                        Notification notification = new Notification("Game over");
                        connections.broadcast("", command.getGameID(), notification);
                    } else {
                        Notification notification = new Notification("Player" + authDao.getAuth(command.getAuthString()).getUsername() + "made move" + move.toString());
                        connections.broadcast(command.getAuthString(), command.getGameID(), notification);
                    }
                } else {
                    Error error = new Error("Player not in game");
                    session.getRemote().sendString(new Gson().toJson(error));
                }

            }

        } catch (DataErrorException | DataAccessException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        } catch (InvalidMoveException e) {
            Error error = new Error("Invalid move");
            session.getRemote().sendString(new Gson().toJson(error));
            //throw new RuntimeException(e);
        }
    }

    private void leaveGame(LeaveCmd command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try {
            if (authDao.findAuth(command.getAuthString())) {
                GameData game = gameDao.getGame(command.getGameID());
                if (game.getWhiteUsername() != null && game.getWhiteUsername().equals(authDao.getAuth(command.getAuthString()).getUsername())) {
                    game.setWhiteUsername(null);

                    gameDao.gameOverride(game);
                    Notification notification = new Notification("white player: " + game.getWhiteUsername() +"left game");
                    connections.broadcast(command.getAuthString(), command.getGameID(), notification);
                   connections.remove(command.getAuthString());
                } else if (game.getBlackUsername() != null && game.getBlackUsername().equals(authDao.getAuth(command.getAuthString()).getUsername())) {
                    game.setBlackUsername(null);
                    gameDao.gameOverride(game);

                    Notification notification = new Notification("black: " + game.getBlackUsername()+ "left game");
                    connections.broadcast(command.getAuthString(), command.getGameID(), notification);
                    connections.remove(command.getAuthString());


                }else {
//                    gameDao.gameOverride(game);
                    Notification notification = new Notification("Observer: " + authDao.getAuth(command.getAuthString()).getUsername() + "left game");
                    connections.broadcast(command.getAuthString(), command.getGameID(), notification);
                    connections.remove(command.getAuthString());

                }

            }
            else{
                Error error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataErrorException | DataAccessException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }


    private void resignGame(ResignCmd command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try {
            if (authDao.findAuth(command.getAuthString())) {
                GameData game = gameDao.getGame(command.getGameID());
                if (game.isGameOver()) {
                    Error error = new Error("Game already over");
                    session.getRemote().sendString(new Gson().toJson(error));
                } else {
                    if ((Objects.equals(game.getWhiteUsername(), authDao.getAuth(command.getAuthString()).getUsername())) || (Objects.equals(game.getBlackUsername(), authDao.getAuth(command.getAuthString()).getUsername()))) {
                        game.setWhiteUsername(null);
                        game.setBlackUsername(null);
                        game.setGameOver(true);

                        gameDao.gameOverride(game);
                        Notification notification = new Notification("Resign player from game: " + authDao.getAuth(command.getAuthString()).getUsername());
                        connections.broadcast("", command.getGameID(), notification);

                    } else {
                        Error error = new Error("Player not in game");
                        session.getRemote().sendString(new Gson().toJson(error));
                    }

                }
            }
            else{
                Error error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataErrorException | DataAccessException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }
}
