package model;

public class AuthData {

    private String username;
    private String authToken;

    public AuthData() {

    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }




    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!AuthData.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final AuthData other = (AuthData) obj;

        if ((this.username == null) ? (other.username != null) : !this.username.equals(other.username)) {
            return false;
        }
        if ((this.authToken == null) ? (other.authToken != null) : !this.authToken.equals(other.authToken)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", AuthToken: " + authToken;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.username != null ? this.username.hashCode() : 0);
        hash = 53 * hash + (this.authToken != null ? this.authToken.hashCode() : 0);
        return hash;
    }
}
