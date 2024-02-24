package model;

public class RegisterResult {

    private String authToken;
    private String username;
    private String message;

    public RegisterResult(String username, String authToken) {
        this.authToken = authToken;
        this.username = username;
    }


    public String getAuthToken() {
        return authToken;
    }






}
