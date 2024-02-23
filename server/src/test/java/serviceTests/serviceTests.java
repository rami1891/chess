package serviceTests;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GameService;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

public class serviceTests {
    static final GameService gameService = new GameService();


    @BeforeEach
    public void clear() throws Exception{
        gameService.clear();
    }

    // Add tests here
    @Test
    public void testRegisterPositive() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        assertNotNull(gameService.register(request).getAuthToken());

    }

    @Test
    public void testRegisterNegative() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);

        registerRequest request2 = new registerRequest("rami", "password", "email");
        assertThrows(Exception.class, () -> gameService.register(request2));
    }

    @Test
    public void testLoginPositive() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        assertNotNull(gameService.login(request2).getAuthToken());
    }

    @Test
    public void testLoginNegative() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("ramiarafeh", "password");
        assertThrows(Exception.class, () -> gameService.login(request2));
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        createGameRequest request3 = new createGameRequest("rami", authToken);
        assert(gameService.createGame(request3).getGameID() >= 0);


    }

    @Test
    public void testCreateGameNegative() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        createGameRequest request3 = new createGameRequest("rami", "wrongAuthToken");
        assertThrows(Exception.class, () -> gameService.createGame(request3));
    }

    @Test
    public void testClear() throws Exception {
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        gameService.clear();
        registerRequest request2 = new registerRequest("rami", "password", "email");
        assertNotNull(gameService.register(request2).getAuthToken());
    }

    @Test
    public void testListGamesPositive() throws Exception{
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        createGameRequest request3 = new createGameRequest("rami", authToken);
        gameService.createGame(request3);
        listGamesRequest request4 = new listGamesRequest(authToken);
        assertNotNull(gameService.listGames(request4));
    }

    @Test
    public void testListGamesNegative() throws Exception{
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        listGamesRequest request4 = new listGamesRequest("wrongAuthToken");
        assertThrows(Exception.class, () -> gameService.listGames(request4));
    }

    @Test
    public void testLogoutPositive() throws Exception{
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        gameService.logout(authToken);
        loginRequest request3 = new loginRequest("rami", "password");
        assertNotNull(gameService.login(request3).getAuthToken());
    }

    @Test
    public void testLogoutNegative() throws Exception{
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        gameService.login(request2);
        assertThrows(Exception.class, () -> gameService.logout("wrongAuthToken"));
    }

    @Test
    public void testJoinPositive() throws Exception{
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        createGameRequest request3 = new createGameRequest("rami", authToken);

        joinGameRequest request6 = new joinGameRequest("BLACK", authToken, gameService.createGame(request3).getGameID());
        assertNotNull(gameService.joinGame(request6).getAuthToken());
    }

    @Test
    public void testJoinNegative() throws Exception{
        registerRequest request = new registerRequest("rami", "password", "email");
        gameService.register(request);
        loginRequest request2 = new loginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        createGameRequest request3 = new createGameRequest("rami", authToken);

        joinGameRequest request6 = new joinGameRequest("BLACK", authToken, 0);
        assertThrows(Exception.class, () -> gameService.joinGame(request6));
    }









}
