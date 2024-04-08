package serviceTests;

import dataAccess.*;
import model.requests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GameService;

import static org.junit.jupiter.api.Assertions.*;


public class serviceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    static GameService gameService = null;

    public serviceTests() {
        UserDAO userDAO = new UserDAOMem();
        AuthDAO authDAO = new AuthDAOMem();
        GameDAO gameDAO = new GameDAOMem();
        gameService = new GameService(userDAO, authDAO, gameDAO);
    }


    @BeforeEach
    public void clear() throws Exception{
        gameService.clear();
    }

    // Add tests here
    @Test
    public void testRegisterPositive() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        assertNotNull(gameService.register(request).getAuthToken());

    }

    @Test
    public void testRegisterNegative() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);

        RegisterRequest request2 = new RegisterRequest("rami", "password", "email");
        assertThrows(Exception.class, () -> gameService.register(request2));
    }

    @Test
    public void testLoginPositive() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        assertNotNull(gameService.login(request2).getAuthToken());
    }

    @Test
    public void testLoginNegative() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("ramiarafeh", "password");
        assertThrows(Exception.class, () -> gameService.login(request2));
    }

    @Test
    public void testCreateGamePositive() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        CreateGameRequest request3 = new CreateGameRequest("rami", authToken);
        assert(gameService.createGame(request3).getGameID() >= 0);


    }

    @Test
    public void testCreateGameNegative() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        CreateGameRequest request3 = new CreateGameRequest("rami", "wrongAuthToken");
        assertThrows(Exception.class, () -> gameService.createGame(request3));
    }

    @Test
    public void testClear() throws Exception {
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        gameService.clear();
        RegisterRequest request2 = new RegisterRequest("rami", "password", "email");
        assertNotNull(gameService.register(request2).getAuthToken());
    }

    @Test
    public void testListGamesPositive() throws Exception{
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        CreateGameRequest request3 = new CreateGameRequest("rami", authToken);
        gameService.createGame(request3);
        ListGamesRequest request4 = new ListGamesRequest(authToken);
        assertNotNull(gameService.listGames(request4));
    }

    @Test
    public void testListGamesNegative() throws Exception{
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        ListGamesRequest request4 = new ListGamesRequest("wrongAuthToken");
        assertThrows(Exception.class, () -> gameService.listGames(request4));
    }

    @Test
    public void testLogoutPositive() throws Exception{
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        gameService.logout(authToken);
        LoginRequest request3 = new LoginRequest("rami", "password");
        assertNotNull(gameService.login(request3).getAuthToken());
    }

    @Test
    public void testLogoutNegative() throws Exception{
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        gameService.login(request2);
        assertThrows(Exception.class, () -> gameService.logout("wrongAuthToken"));
    }

    @Test
    public void testJoinPositive() throws Exception{
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        CreateGameRequest request3 = new CreateGameRequest("rami", authToken);

        JoinGameRequest request6 = new JoinGameRequest("BLACK", authToken, gameService.createGame(request3).getGameID());
        assertNotNull(gameService.joinGame(request6).getAuthToken());
    }

    @Test
    public void testJoinNegative() throws Exception{
        RegisterRequest request = new RegisterRequest("rami", "password", "email");
        gameService.register(request);
        LoginRequest request2 = new LoginRequest("rami", "password");
        String authToken = gameService.login(request2).getAuthToken();
        CreateGameRequest request3 = new CreateGameRequest("rami", authToken);

        JoinGameRequest request6 = new JoinGameRequest("BLACK", authToken, 0);
        assertThrows(Exception.class, () -> gameService.joinGame(request6));
    }









}
