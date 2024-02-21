package model;

public class createGameRequest {
    private String gameName;
    private String authToken;

    public createGameRequest(String gameName, String authToken) {
        this.gameName = gameName;
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;

    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}
