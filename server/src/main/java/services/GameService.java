package services;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.AuthData;

import spark.*;

import model.registerRequest;
import com.google.gson.Gson;
import model.registerResult;

import model.UserData;

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

    public registerResult register(registerRequest request) throws DataAccessException {
        if(request.getUsername() == null || request.getPassword() == null || request.getEmail() == null){
            throw new DataAccessException("Invalid input");
        }

        if(request.getUsername().equals("") || request.getPassword().equals("") || request.getEmail().equals("")){
            throw new DataAccessException("Invalid input");
        }

        if(userDAO.findUser(request.getUsername())){
            throw new DataAccessException("Username already exists");
        }



        UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
        userDAO.createUser(user);
        AuthData myAuth = new AuthData();
        myAuth.setUsername(request.getUsername());
        authDAO.createAuth(myAuth);

        registerResult registerResult = new registerResult(request.getUsername(), myAuth.getAuthToken());
        return registerResult;


    }





}
