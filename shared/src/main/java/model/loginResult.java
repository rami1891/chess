package model;

public class loginResult {

        private String authToken;
        private String username;
        private String message;

        public loginResult(String authToken, String username) {
            this.authToken = authToken;
            this.username = username;
        }



        public loginResult(String message) {
            this.message = message;
        }

        public String getAuthToken() {
            return authToken;
        }

        public String getUsername() {
            return username;
        }
}
