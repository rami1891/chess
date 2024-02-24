package model;

public class JoinGameRequest {
    private String playerColor;
    private String authToken;
    private int gameID;

    public JoinGameRequest(String playerColor, String authToken, int gameID) {
        this.playerColor = playerColor;
        this.authToken = authToken;
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
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





}
