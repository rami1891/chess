package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCmd extends UserGameCommand {
    private final int gameID;
    private final ChessGame.TeamColor playerColor;

    public JoinPlayerCmd(String authToken, int GameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = GameID;
        this.playerColor = playerColor;
        this.commandType = CommandType.JOIN_PLAYER;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

}
