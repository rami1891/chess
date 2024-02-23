package model;

public class registerResult {

    private String authToken;
    private String username;
    private String message;

    public registerResult(String username, String authToken) {
        this.authToken = authToken;
        this.username = username;
    }


    public String getAuthToken() {
        return authToken;
    }






}
