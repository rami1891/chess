package model;
import chess.ChessGame;

import chess.ChessGame;

public class GameData {

    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public GameData(String gameName) {
        this.gameName = gameName;
        this.game = new ChessGame();



    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }


    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!GameData.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final GameData other = (GameData) obj;

        if (this.gameID != other.gameID) {
            return false;
        }
        if ((this.whiteUsername == null) ? (other.whiteUsername != null) : !this.whiteUsername.equals(other.whiteUsername)) {
            return false;
        }
        if ((this.blackUsername == null) ? (other.blackUsername != null) : !this.blackUsername.equals(other.blackUsername)) {
            return false;
        }
        if ((this.gameName == null) ? (other.gameName != null) : !this.gameName.equals(other.gameName)) {
            return false;
        }
        if ((this.game == null) ? (other.game != null) : !this.game.equals(other.game)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameID: " + gameID + ", WhiteUsername: " + whiteUsername + ", BlackUsername: " + blackUsername + ", GameName: " + gameName + ", Game: " + game;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.gameID;
        hash = 53 * hash + (this.whiteUsername != null ? this.whiteUsername.hashCode() : 0);
        hash = 53 * hash + (this.blackUsername != null ? this.blackUsername.hashCode() : 0);
        hash = 53 * hash + (this.gameName != null ? this.gameName.hashCode() : 0);
        hash = 53 * hash + (this.game != null ? this.game.hashCode() : 0);
        return hash;
    }

}
