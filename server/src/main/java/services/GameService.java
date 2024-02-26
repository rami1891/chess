package services;

import dataAccess.*;
import model.*;


import model.requests.*;
import model.results.CreateGameResult;
import model.results.JoinGameResult;
import model.results.LoginResult;
import model.results.RegisterResult;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GameService {

    RegisterRequest registerRequest = new RegisterRequest();
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    GameDAO gameDAO = new GameDAO();


    /**
     * Clears the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException {
        userDAO.deleteUser();
        authDAO.deleteAuth();
        gameDAO.deleteGame();

    }


    /**
     * Registers a new user in the database and returns an auth token
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public RegisterResult register(RegisterRequest request) throws DataAccessException, DataErrorException{
        if(request.getUsername() == null || request.getPassword() == null || request.getEmail() == null){
            //throw new DataAccessException("Invalid input");
            throw new DataErrorException(400, "Error: bad request");
        }

        if(request.getUsername().equals("") || request.getPassword().equals("") || request.getEmail().equals("")){
            throw new DataErrorException(400, "Error: bad request");
        }

        if(userDAO.findUser(request.getUsername())){
            throw new DataErrorException(403,"Error: already taken");
        }

        UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
        userDAO.createUser(user);
        AuthData myAuth = new AuthData();
        myAuth.setUsername(request.getUsername());
        String auth = UUID.randomUUID().toString();
        while(authDAO.findAuth(auth)){
            auth = UUID.randomUUID().toString();
        }
        myAuth.setAuthToken(auth);
        authDAO.createAuth(myAuth);

        RegisterResult registerResult = new RegisterResult(request.getUsername(), myAuth.getAuthToken());
        return registerResult;


    }


    /**
     * Logs in a user and returns an auth token
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public LoginResult login(LoginRequest request) throws DataAccessException, DataErrorException {
        if(request.getUsername() == null || request.getPassword() == null){
            throw new DataErrorException(401, "Error: unauthorized");
        }

        if(request.getUsername().equals("") || request.getPassword().equals("")) {
            throw new DataErrorException(401, "Error: unauthorized");
        }

        if(!userDAO.findUser(request.getUsername())){
            throw new DataErrorException(401, "Error: unauthorized");


        }

        if(!userDAO.readUser(request.getUsername()).getPassword().equals(request.getPassword())){
            throw new DataErrorException(401, "Error: unauthorized");

        }

        AuthData myAuth = new AuthData();
        myAuth.setUsername(request.getUsername());
        String auth = UUID.randomUUID().toString();
        while(authDAO.findAuth(auth)){
            auth = UUID.randomUUID().toString();
        }
        myAuth.setAuthToken(auth);
        authDAO.createAuth(myAuth);

        LoginResult loginResult = new LoginResult(request.getUsername(), myAuth.getAuthToken());
        return loginResult;

    }

    /**
     * Logs out a user
     * @param request
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public void logout(String request) throws DataAccessException, DataErrorException {
        if(authDAO.deleteMyAuth(request) == null){
            throw new DataErrorException(401, "Error: unauthorized");
        }
    }


    /**
     * Lists all the games in the database
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public Collection<GameData> listGames(ListGamesRequest request) throws DataAccessException, DataErrorException {

        if(request.getAuthToken() == null || !authDAO.findAuth(request.getAuthToken())){
            throw new DataErrorException(401, "Error: unauthorized");
        }

        Collection<GameData> games = gameDAO.listGames();
        if(games == null){
            throw new DataErrorException(401, "Error: internal server error");
        }
        return games;
    }



    /**
     * Creates a new game in the database
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException, DataErrorException {
        if(request.getGameName() == null || request.getGameName().equals("")){
            throw new DataErrorException(400, "Error: bad request");
        }

        if(gameDAO.findGame(request.getGameName())){
            throw new DataErrorException(401, "Error: already taken");
        }

        if(!authDAO.findAuth(request.getAuthToken())){
            throw new DataErrorException(401, "Error: unauthorized");
        }


        GameData game = new GameData(request.getGameName());
        gameDAO.createGame(game);

        int newGameID = ThreadLocalRandom.current().nextInt();
        while(newGameID <=0){
            newGameID = ThreadLocalRandom.current().nextInt();
        }

        game.setGameID(newGameID);

        CreateGameResult createGameResult = new CreateGameResult(game.getGameID());
        return createGameResult;

    }


    /**
     * Joins a game in the database
     * @param request
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException, DataErrorException {
        if(request.getGameID() <= 0){
            throw new DataErrorException(400, "Error: bad request");
        }

        if(!authDAO.findAuth(request.getAuthToken())){
            throw new DataErrorException(401, "Error: unauthorized");
        }


        if(gameDAO.getGame(request.getGameID()) == null){
            throw new DataErrorException(401, "Error: unauthorized");
        }

        if(request.getPlayerColor() == null || request.getPlayerColor().equals("")){
            JoinGameResult joinGameResult = new JoinGameResult(request.getAuthToken(), request.getGameID(), null, null);
            return joinGameResult;
        }

        if(gameDAO.getGame(request.getGameID()).getBlackUsername() != null && gameDAO.getGame(request.getGameID()).getWhiteUsername() != null){
            throw new DataErrorException(403, "Error: already taken");
        }

        if(request.getPlayerColor().equals("WHITE") && gameDAO.getGame(request.getGameID()).getWhiteUsername() == null){
            gameDAO.getGame(request.getGameID()).setWhiteUsername(authDAO.getAuth(request.getAuthToken()).getUsername());
            JoinGameResult joinGameResult = new JoinGameResult(request.getAuthToken(), request.getGameID(), "white", null);
            return joinGameResult;
        }
        else if(request.getPlayerColor().equals("BLACK") && gameDAO.getGame(request.getGameID()).getBlackUsername() == null){
            gameDAO.getGame(request.getGameID()).setBlackUsername(authDAO.getAuth(request.getAuthToken()).getUsername());
            JoinGameResult joinGameResult = new JoinGameResult(request.getAuthToken(), request.getGameID(), "black", null);
            return joinGameResult;
        }
        else{
            throw new DataErrorException(403, "Error: bad request");
        }
    }





}
