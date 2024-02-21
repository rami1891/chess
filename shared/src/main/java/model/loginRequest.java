package model;

public class loginRequest {

    private String username;
    private String password;

    public loginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public loginRequest() {
        this.username = null;
        this.password = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
