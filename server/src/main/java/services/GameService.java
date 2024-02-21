package services;

import dataAccess.*;
import model.*;


import spark.*;

import model.registerRequest;
import com.google.gson.Gson;

import java.awt.font.GlyphMetrics;
import java.util.Collection;
import java.util.UUID;

public class GameService {

    registerRequest registerRequest = new registerRequest();
    UserDAO userDAO = new UserDAO();
    AuthDAO authDAO = new AuthDAO();
    GameDAO gameDAO = new GameDAO();

    public void clear() throws DataAccessException {
        userDAO.deleteUser();
        authDAO.deleteAuth();
        gameDAO.deleteGame();

    }

    public registerResult register(registerRequest request) throws DataAccessException, DataErrorException{
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
        myAuth.setAuthToken(UUID.randomUUID().toString());
        authDAO.createAuth(myAuth);

        registerResult registerResult = new registerResult(request.getUsername(), myAuth.getAuthToken());
        return registerResult;


    }

    public loginResult login(loginRequest request) throws DataAccessException, DataErrorException {
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
        myAuth.setAuthToken(UUID.randomUUID().toString());
        authDAO.createAuth(myAuth);

        loginResult loginResult = new loginResult(request.getUsername(), myAuth.getAuthToken());
        return loginResult;

    }

    public void logout(String request) throws DataAccessException, DataErrorException {
//        authDAO.deleteMyAuth(request);
        if(authDAO.deleteMyAuth(request) == null){
            throw new DataErrorException(401, "Error: unauthorized");
        }
    }

    public Collection<GameData> listGames() throws DataAccessException, DataErrorException {
        Collection<GameData> games = gameDAO.listGames();
        if(games == null){
            throw new DataErrorException(401, "Error: internal server error");
        }
        return games;
    }





}
