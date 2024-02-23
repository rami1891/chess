package model;

public class loginResult {

    private String authToken;
    private String username;


    public loginResult(String username, String authToken) {
        this.authToken = authToken;
        this.username = username;
    }


    public String getAuthToken() {
        return authToken;
    }
}


