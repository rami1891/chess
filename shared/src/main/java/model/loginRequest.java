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


}
