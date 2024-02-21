package server;

import spark.*;
import services.GameService;
import com.google.gson.Gson;
import model.registerRequest;
import model.registerResult;
//import exception.ResponseException;

import dataAccess. *;

public class Server {

    private final GameService gameService;

    public Server() {
        this.gameService = new GameService();
    }




    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);





        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) throws DataAccessException {

        gameService.clear();
        res.status(204);

        return "";
    }

    private Object login(Request req, Response res) {
        var request = new Gson().fromJson(req.body(), registerRequest.class);


    }


    private Object register(Request req, Response res) throws DataAccessException {
        var request = new Gson().fromJson(req.body(), registerRequest.class);
        registerResult result = gameService.register(request);




        return new Gson().toJson(result);
    }
}
