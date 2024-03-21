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
    public void testRegister() throws ResponseException {
        facade.register("user1", "password", "email");
    }



}
