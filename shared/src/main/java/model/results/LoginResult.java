package model.results;

public class LoginResult {

    private String authToken;
    private String username;


    public LoginResult(String username, String authToken) {
        this.authToken = authToken;
        this.username = username;
    }


    public String getAuthToken() {
        return authToken;
    }
}


