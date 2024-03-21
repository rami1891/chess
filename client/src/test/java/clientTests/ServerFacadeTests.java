package clientTests;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;


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
        facade.register("user1", "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> facade.register("user1", "password", "email"));

    }

    @Test
    public void testLoginPositive() throws ResponseException {
        facade.register("user1", "password", "email");
        Assertions.assertDoesNotThrow(() -> facade.login("user1", "password"));
    }

    @Test
    public void testLoginNegative() throws ResponseException {
        facade.register("user1", "password", "email");

        Assertions.assertThrows(ResponseException.class, () -> facade.login("user2", "password"));
    }

    @Test
    public void testLogoutPositive() throws ResponseException {
        facade.register("user1", "password", "email");
        facade.login("user1", "password");
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void testLogoutNegative() throws ResponseException {
        facade.register("user1", "password", "email");
        facade.logout();
        Assertions.assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    public void testCreateGamePositive() throws ResponseException {
        facade.register("user1", "password", "email");
        facade.login("user1", "password");
        Assertions.assertDoesNotThrow(() -> facade.createGame("game1"));
    }

    @Test
    public void testCreateGameNegative() throws ResponseException {
        facade.register("user1", "password", "email");
        facade.login("user1", "password");
        facade.createGame("game1");
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("game1"));
    }










}
