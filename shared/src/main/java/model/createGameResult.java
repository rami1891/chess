package model;

public class createGameResult {
    private int gameID;
    private String authToken;

    public createGameResult(int gameID) {
        this.gameID = gameID;
        //this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


}
