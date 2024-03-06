package dataAccessTests;

import dataAccess.DataErrorException;
import dataAccess.MySqlAuthDAO;
import dataAccess.MySqlGameDAO;
import dataAccess.MySqlUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class dataAccessTests {
    static final MySqlGameDAO gameDAO;
    static final MySqlUserDAO userDAO;
    static final MySqlAuthDAO authDAO;

    static {
        try {
            gameDAO = new MySqlGameDAO();
            userDAO = new MySqlUserDAO();
            authDAO = new MySqlAuthDAO();

        } catch (DataErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void clear() throws Exception {
        gameDAO.deleteGame();
        userDAO.deleteUser();
        authDAO.deleteAuth();
    }

    // MySqlUserDAO tests
    @Test
    public void testCreateUserPositive() throws Exception {
        userDAO.createUser(new UserData("rami", "password", "email"));
        assertNotNull(userDAO.readUser("rami"));
    }

    @Test
    public void testCreateUserNegative() throws Exception {
        userDAO.createUser(new UserData("rami", "password", "email"));
        assertThrows(Exception.class, () -> userDAO.createUser(new UserData("rami", "password", "email")));
    }

    @Test
    public void testReadUserPositive() throws Exception {
        userDAO.createUser(new UserData("rami", "password", "email"));
        assertNotNull(userDAO.readUser("rami"));
    }

    @Test
    public void testReadUserNegative() throws Exception {
        assertNull(userDAO.readUser("rami"));
    }

    @Test
    public void testDeleteUserPositive() throws Exception {
        userDAO.createUser(new UserData("rami", "password", "email"));
        userDAO.deleteUser();
        assertNull(userDAO.readUser("rami"));
    }


    @Test
    public void testFindUserPositive() throws Exception {
        userDAO.createUser(new UserData("rami", "password", "email"));
        assertTrue(userDAO.findUser("rami"));
    }

    @Test
    public void testFindUserNegative() throws Exception {
        assertFalse(userDAO.findUser("rami"));
    }


    // MySqlAuthDAO tests
    @Test
    public void testCreateAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);

        assertNotNull(authDAO.getAuth("auth"));
    }

    @Test
    public void testCreateAuthNegative() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        assertThrows(Exception.class, () -> authDAO.createAuth(auth));
    }

    @Test
    public void testGetAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        assertNotNull(authDAO.getAuth("auth"));
    }

    @Test
    public void testGetAuthNegative() throws Exception {
        assertNull(authDAO.getAuth("auth"));
    }

    @Test
    public void testDeleteAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        authDAO.deleteAuth();
        assertNull(authDAO.getAuth("auth"));
    }


    @Test
    public void testDeleteMyAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        authDAO.deleteMyAuth("auth");
        assertNull(authDAO.getAuth("auth"));
    }


    @Test
    public void testDeleteMyAuthNegative() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        assertThrows(Exception.class, () -> authDAO.deleteMyAuth("wrongauth"));
    }

    @Test
    public void testFindAuthPositive() throws Exception {
        AuthData auth = new AuthData();
        auth.setUsername("rami");
        auth.setAuthToken("auth");
        authDAO.createAuth(auth);
        assertTrue(authDAO.findAuth("auth"));
    }

    @Test
    public void testFindAuthNegative() throws Exception {
        assertFalse(authDAO.findAuth("auth"));
    }


    // MySqlGameDAO tests

    @Test
    public void testCreateGamePositive() throws Exception {
        GameData game = new GameData("game");
        gameDAO.createGame(game);
        assertNotNull(gameDAO.listGames());

    }

    @Test
    public void testCreateGameNegative() throws Exception {
        GameData game = new GameData("game");
        gameDAO.createGame(game);
        assertThrows(Exception.class, () -> gameDAO.createGame(game));
    }

    @Test
    public void testListGamesPositive() throws Exception {
        GameData game = new GameData("game");
        gameDAO.createGame(game);
        assertNotNull(gameDAO.listGames());
    }

    @Test
    public void testListGamesNegative() throws Exception {
        assertTrue(gameDAO.listGames().isEmpty());
    }


    @Test
    public void testDeleteGamePositive() throws Exception {
        GameData game = new GameData("game");
        gameDAO.createGame(game);
        gameDAO.deleteGame();
        assertTrue(gameDAO.listGames().isEmpty());
    }


    @Test
    public void testJoinGamePositive() throws Exception {
        GameData game = new GameData(1, "white", "black", "game", null);
        gameDAO.createGame(game);
        gameDAO.joinGame(game);
        assertNotNull(gameDAO.listGames());
    }

    @Test
    public void testJoinGameNegative() throws Exception {
        GameData game = new GameData("game");
        gameDAO.createGame(game);
        game.setGameID(-1);
        assertThrows(Exception.class, () -> gameDAO.joinGame(game));
    }

    @Test
    public void testFindGamePositive() throws Exception {
        GameData game = new GameData("game");
        gameDAO.createGame(game);

        assertTrue(gameDAO.findGame("game"));
    }

    @Test
    public void testFindGameNegative() throws Exception {
        assertFalse(gameDAO.findGame("game"));
    }

    @Test
    public void testGetGamePositive() throws Exception {
        GameData game = new GameData(1, "white", "black", "game", null);
        gameDAO.createGame(game);

        assertNotNull(gameDAO.getGame(1));
    }

    @Test
    public void testGetGameNegative() throws Exception {
        assertNull(gameDAO.getGame(1));
    }







}
