package ui;

import exception.ResponseException;
import model.requests.*;
import model.results.*;
import server.Server;
import com.google.gson.Gson;
//import exception.ResponseException;

import java.io.*;
import java.net.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ServerFacade {
    private final String serverUrl;
    static String authToken = null;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        RegisterResult result = this.makeRequest("POST", path, new RegisterRequest(username, password, email), RegisterResult.class);
        authToken = result.getAuthToken();

    }

    public void login(String username, String password) throws ResponseException {
        var path = "/session";
        LoginResult result = this.makeRequest("POST", path, new LoginRequest(username, password), LoginResult.class);
        authToken = result.getAuthToken();

    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
        authToken = null;

    }

    public void createGame(String gameName) throws ResponseException {
        var path = "/game";

        this.makeRequest("POST", path, new CreateGameRequest(gameName, authToken), CreateGameResult.class);
    }

    public Collection listGames() throws ResponseException {
        var path = "/game";
        ListGamesResult result = this.makeRequest("GET", path, new ListGamesRequest(authToken), ListGamesResult.class);
        return result.getGames();
    }

    public void joinObserver(int gameId) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, new JoinGameRequest(null, authToken, gameId), JoinGameResult.class);
    }

    public void joinGame(int gameId, String playerColor) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, new JoinGameRequest(playerColor, authToken, gameId), JoinGameResult.class);

    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            // http set header
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            if(Objects.equals(method, "GET")){
                http.setDoOutput(false);
            } else {
                http.setDoOutput(true);
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }


    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
