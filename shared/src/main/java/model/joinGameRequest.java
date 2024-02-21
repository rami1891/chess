package model;

public class joinGameRequest {
    private String playerColor;
    private String authToken;
    private int gameID;

    public joinGameRequest(String playerColor, String authToken, int gameID) {
        this.playerColor = playerColor;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }




}
