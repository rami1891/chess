package server;

import model.*;
import spark.*;
import services.GameService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import dataAccess.DataErrorException;

public class Server {

    private final GameService gameService;
    public Server() {
        this.gameService = new GameService();
    }

    /**
     * Starts the server
     * @param desiredPort
     * @return
     */
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game", this::joinGame);

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }


    /**
     * Stops the server
     */
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    
    /**
     * Joins a game
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object joinGame(Request req, Response res) throws DataAccessException, DataErrorException {
        try{
            String AuthToken = req.headers("Authorization");

            var request = new Gson().fromJson(req.body(), joinGameRequest.class);
            request.setAuthToken(AuthToken);
            joinGameResult result = gameService.joinGame(request);
            return new Gson().toJson(result);
        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 400){
                res.status(400);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Bad Request");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 403){
                res.status(403);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Already Taken");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
        }
    }

    /**
     * Creates a game
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object createGame(Request req, Response res) throws DataAccessException, DataErrorException {
        try{
            String AuthToken = req.headers("Authorization");

            var request = new Gson().fromJson(req.body(), createGameRequest.class);
            request.setAuthToken(AuthToken);
           createGameResult result =  gameService.createGame(request);
            return new Gson().toJson(result);

        }
        catch (DataErrorException e){

            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 400){
                res.status(400);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Bad Request");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
        }
    }


    /**
     * Lists all the games
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object listGames(Request req, Response res) throws DataAccessException {
        try{

            String AuthToken = req.headers("Authorization");
            listGamesRequest request = new listGamesRequest(AuthToken);

            listGamesResult games = new listGamesResult(gameService.listGames(request));

            return new Gson().toJson(games);

        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }

        }


    }

    /**
     * Clears the database
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     */
    private Object clear(Request req, Response res) throws DataAccessException {

        gameService.clear();
        return "";
    }


    /**
     * Logs in a user
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object login(Request req, Response res) throws DataAccessException, DataErrorException{
        try {
            var request = new Gson().fromJson(req.body(), loginRequest.class);
            loginResult result = gameService.login(request);

            return new Gson().toJson(result);
        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
        }

    }


    /**
     * Registers a user
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object register(Request req, Response res) throws DataAccessException, DataErrorException {
        try {
            var request = new Gson().fromJson(req.body(), registerRequest.class);
            registerResult result = gameService.register(request);
            return new Gson().toJson(result);
        }


        catch (DataErrorException e){
            if(e.getErrorCode() == 400){
                res.status(400);
                //return new Gson().toJson(e.getMessage());


                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Bad Request");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 403){
                res.status(403);

                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Already Taken");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);

                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
        }
    }


    /**
     * Logs out a user
     * @param req
     * @param res
     * @return
     * @throws DataAccessException
     * @throws DataErrorException
     */
    private Object logout(Request req, Response res) throws DataAccessException, DataErrorException {
        try {
            gameService.logout(req.headers("Authorization"));
            return "";
        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
        }
    }
}
