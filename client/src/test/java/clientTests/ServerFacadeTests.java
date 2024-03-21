package clientTests;

import dataAccess.DataErrorException;
import dataAccess.GameDAO;
import dataAccess.MySqlGameDAO;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;


    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
        facade.clear();

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterPositive() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.register("user1", "password", "email"));
    }

    @Test
    public void testRegisterNegative() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> facade.register("user1", "password", "email"));

    }

    @Test
    public void testLoginPositive() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.login("user1", "password"));
    }

    @Test
    public void testLoginNegative() throws ResponseException {

        Assertions.assertThrows(ResponseException.class, () -> facade.login("user2", "password"));
    }

    @Test
    public void testLogoutPositive() throws ResponseException {
        facade.login("user1", "password");
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void testLogoutNegative() throws ResponseException {
        facade.login("user1", "password");
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    public void testCreateGamePositive() throws ResponseException {
        facade.register("userq", "password", "email");
        facade.login("userq", "password");
        Assertions.assertDoesNotThrow(() -> facade.createGame("game3"));
    }

    @Test
    public void testCreateGameNegative() throws ResponseException {
        facade.register("userw", "password", "email");
        facade.login("userw", "password");
        facade.createGame("gamere");
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("gamere"));
    }

    @Test
    public void testJoinGamePositive() throws ResponseException, DataErrorException {
        facade.register("usera", "password", "email");

        facade.login("usera", "password");
        facade.createGame("gamea");
        Collection<GameData> games = facade.listGames();
        if(games.contains("gamea")){
            for(GameData game : games){
                if(game.getGameName().equals("gamea")){
                    int gameId = game.getGameID();
                    Assertions.assertDoesNotThrow(() -> facade.joinGame(gameId, "white"));
                }
            }
        }
    }

    @Test
    public void testJoinGameNegative() throws ResponseException {
        facade.register("user2", "password", "email");
        facade.login("user2", "password");
        facade.createGame("gamed");
        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(1, "white"));
    }

    @Test
    public void testJoinObserverPositive() throws ResponseException, DataErrorException {
        facade.login("user1", "password");
        facade.createGame("game1");
        Collection<GameData> games = facade.listGames();
        if(games.contains("gamec")){
            for(GameData game : games){
                if(game.getGameName().equals("game1")){
                    int gameId = game.getGameID();
                    Assertions.assertDoesNotThrow(() -> facade.joinObserver(gameId));
                }
            }
        }
    }

    @Test
    public void testJoinObserverNegative() throws ResponseException {
        facade.register("userg", "password", "email");
        facade.login("userg", "password");
        facade.createGame("gamev");
        Assertions.assertThrows(ResponseException.class, () -> facade.joinObserver(1));
    }

    @Test
    public void testListGamesPositive() throws ResponseException {
        facade.register("user4", "password", "email");
        facade.login("user4", "password");
        facade.createGame("gameh");
        Assertions.assertNotNull(facade.listGames());
    }

    @Test
    public void testListGamesNegative() throws ResponseException {
        facade.register("user5", "password", "email");
        facade.login("user5", "password");
        facade.createGame("gamei");
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.listGames());
    }


    @Test
    public void testClear() throws ResponseException {
        Assertions.assertDoesNotThrow(() -> facade.clear());
    }










}
