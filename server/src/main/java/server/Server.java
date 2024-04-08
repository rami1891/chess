package server;

import dataAccess.*;
import model.requests.*;
import model.results.*;
import server.webSockets.WebSocketHandler;
import spark.*;
import services.GameService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Server {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private WebSocketHandler webSocketHandler;

    private final GameService gameService;
    public Server() {
        try {
            userDAO = new MySqlUserDAO();
            authDAO = new MySqlAuthDAO();
            gameDAO = new MySqlGameDAO();
            this.gameService = new GameService(userDAO, authDAO, gameDAO);
             webSocketHandler = new WebSocketHandler(gameDAO, authDAO, userDAO);
        }
        catch (DataErrorException e){
            throw new RuntimeException("Error: " + e.getMessage());
        }


    }

    /**
     * Starts the server
     * @param desiredPort
     * @return
     */
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", webSocketHandler);
        Spark.init();

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
            String authToken = req.headers("Authorization");

            var request = new Gson().fromJson(req.body(), JoinGameRequest.class);
            request.setAuthToken(authToken);
            JoinGameResult result = gameService.joinGame(request);
            return new Gson().toJson(result);
        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized join game");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 400){
                res.status(400);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Bad Request to Join Game");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 403){
                res.status(403);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Already Taken for Join Game ");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description of Join Game");
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
            String authToken = req.headers("Authorization");

            var request = new Gson().fromJson(req.body(), CreateGameRequest.class);
            request.setAuthToken(authToken);
            CreateGameResult result =  gameService.createGame(request);
            return new Gson().toJson(result);

        }
        catch (DataErrorException e){

            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized create game");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 400){
                res.status(400);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Bad Request to Create Game");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description of Create Game");
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

            String authToken = req.headers("Authorization");
            ListGamesRequest request = new ListGamesRequest(authToken);

            ListGamesResult games = new ListGamesResult(gameService.listGames(request));

            return new Gson().toJson(games);

        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description of List Games");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description of List Games");
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
    private Object clear(Request req, Response res) throws DataAccessException, DataErrorException {

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
            var request = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult result = gameService.login(request);

            return new Gson().toJson(result);
        }
        catch (DataErrorException e){
            if(e.getErrorCode() == 401){
                res.status(401);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized login");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized login");
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
            var request = new Gson().fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = gameService.register(request);
            return new Gson().toJson(result);
        }


        catch (DataErrorException e){
            if(e.getErrorCode() == 400){
                res.status(400);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Bad Request to Register");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else if(e.getErrorCode() == 403){
                res.status(403);

                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Already Taken for Register");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);

                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Description of Register");
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
                errorJson.addProperty("error", "Unauthorized logout");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
            else{
                res.status(500);
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("error", "Unauthorized logout");
                errorJson.addProperty("message", e.getMessage());
                return new Gson().toJson(errorJson);
            }
        }
    }
}
