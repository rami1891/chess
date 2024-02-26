package model.results;

public class JoinGameResult {
private String authToken;
    private int gameID;
    private String playerColor;
    private String errorMessage;

    public JoinGameResult(String authToken, int gameID, String playerColor, String errorMessage) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = playerColor;
        this.errorMessage = errorMessage;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


}
