package model;

public class registerRequest {
    private String username;
    private String password;
    private String email;

    public registerRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public registerRequest() {
        this.username = null;
        this.password = null;
        this.email = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
