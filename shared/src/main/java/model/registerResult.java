package model;

public class registerResult {

    private String authToken;
    private String username;
    private String message;

    public registerResult(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public registerResult(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }


    public String getMessage() {
        return message;
    }

    public void createAuthToken(String authToken) {
        this.authToken = authToken;
    }




}
