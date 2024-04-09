package server.webSockets;


import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserverCmd;
import webSocketMessages.userCommands.JoinPlayerCmd;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.UserGameCommand;

import javax.swing.*;
import java.io.IOException;
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
//            case MAKE_MOVE:
//                makeMove(command, session);
//                break;
//            case LEAVE:
//                leaveGame(command, session);
//                break;
//            case RESIGN:
//                resignGame(command, session);
//                break;

        }
    }


    private void joinGame(JoinPlayerCmd command, Session session) throws IOException {
        connections.add(command.getAuthString(), session);
        try{
            if(authDao.findAuth(command.getAuthString())){
                GameData game = gameDao.getGame(command.getGameID());

                if(command.getPlayerColor() == null){
                    Error error = new Error("Player color not specified");
                    session.getRemote().sendString(new Gson().toJson(error));
                }

                else if(((command.getPlayerColor().equals("WHITE")) && game.getWhiteUsername() == null) || !(authDao.getAuth(command.getAuthString()).getUsername().equals(game.getWhiteUsername()))){
                    Error error = new Error("Player color not specified");
                    session.getRemote().sendString(new Gson().toJson(error));
                }
                else if(((command.getPlayerColor().equals("BLACK")) && game.getBlackUsername() == null) || !(authDao.getAuth(command.getAuthString()).getUsername().equals(game.getBlackUsername()))){
                    Error error = new Error("Player color not specified");
                    session.getRemote().sendString(new Gson().toJson(error));
                }

                else if(command.getPlayerColor().equals("WHITE")){
                    game.setWhiteUsername(command.getAuthString());
                    LoadGameMessage message = new LoadGameMessage(game.getGame());
                    session.getRemote().sendString(new Gson().toJson(message));
                    Notification notification = new Notification("Player joined game");
                    connections.broadcast(command.getAuthString(), notification);

                }
                else{
                    game.setBlackUsername(command.getAuthString());
                    LoadGameMessage message = new LoadGameMessage(game.getGame());
                    session.getRemote().sendString(new Gson().toJson(message));
                    Notification notification = new Notification("Player joined game");
                    connections.broadcast(command.getAuthString(), notification);

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
            if(authDao.findAuth(command.getAuthString())){
                GameData game = gameDao.getGame(command.getGameID());
                LoadGameMessage message = new LoadGameMessage(game.getGame());
                session.getRemote().sendString(new Gson().toJson(message));
                Notification notification = new Notification("Observer joined game");
                connections.broadcast(command.getAuthString(), notification);

            }
            else{
                Error error = new Error("Invalid auth token");
                session.getRemote().sendString(new Gson().toJson(error));
            }
        } catch (DataErrorException e ) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        } catch (DataAccessException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }

    }


    private void makeMove(MakeMove command, Session session) throws IOException {
        try {
            if (authDao.findAuth(command.getAuthString())) {
                GameData game = gameDao.getGame(command.getGameID());

            }

        } catch (DataErrorException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        } catch (DataAccessException e) {
            String error = new Gson().toJson(new Error(e.getMessage()));
            session.getRemote().sendString(error);
        }
    }
}
