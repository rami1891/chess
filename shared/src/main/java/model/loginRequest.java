package model;

public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {
        this.username = null;
        this.password = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
