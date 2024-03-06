package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static dataAccess.DatabaseManager.configureDatabase;
import static java.sql.Types.NULL;

public class MySqlGameDAO implements GameDAO{

    /**
     * Constructor
     * @throws DataErrorException
     */
    public MySqlGameDAO() throws DataErrorException {
        configureDatabase();
    }

    /**
     * Creates a new game in the database
     * @param game
     * @throws DataErrorException
     */
    @Override
    public void createGame(GameData game) throws DataErrorException {
        var statement = "INSERT INTO Games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        var gameID = game.getGameID();
        var gameName = game.getGameName();
        var whiteUsername = game.getWhiteUsername();
        var blackUsername = game.getBlackUsername();
        // serializing stuff
         ChessGame chessGame = game.getGame();
         Gson chess = new Gson();
         String chessGameObj = chess.toJson(chessGame);

         // execute statement
        executeStatement(statement, gameID, whiteUsername, blackUsername, gameName, chessGameObj);
    }

    /**
     * Executes a statement
     * @param statement
     * @param gameID
     * @param whiteUsername
     * @param blackUsername
     * @param gameName
     * @param chessGame
     * @throws DataErrorException
     */
    private void executeStatement(String statement, int gameID, String whiteUsername, String blackUsername, String gameName, String chessGame) throws DataErrorException{
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(gameID >= 0)
                stmt.setInt(1, gameID);
            if(whiteUsername != null)
                stmt.setString(2, whiteUsername);
            else stmt.setNull(2, NULL);
            if(blackUsername != null)
                stmt.setString(3, blackUsername);
            else stmt.setNull(3, NULL);
            if(gameName != null)
                stmt.setString(4, gameName);
            else stmt.setNull(4, NULL);
            if(chessGame != null)
                stmt.setString(5, chessGame);
            else stmt.setNull(5, NULL);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }
    }


    /**
     * Lists all games in the database
     * @return
     * @throws DataErrorException
     */
    @Override
    public Collection<GameData> listGames() throws DataErrorException {
        Collection<GameData> games = new ArrayList<GameData>();
        var statement = "SELECT * FROM Games";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){

            var rs = stmt.executeQuery();
            while (rs.next()) {
                var gameID = rs.getInt("gameID");
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                var game = rs.getString("game");
                // serializing stuff
                Gson gson = new Gson();
                ChessGame gameObj = gson.fromJson(game, ChessGame.class);
                games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, gameObj));
            }
            return games;
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when reading game");
        }
    }


    /**
     * Joins a game
     * @param game
     * @throws DataErrorException
     */
    @Override
    public void joinGame(GameData game) throws DataErrorException {
        var statement = "UPDATE Games SET blackUsername = ?, whiteUsername = ? WHERE gameID = ?";
        var gameID = game.getGameID();

        // check if gameID is valid, this is a double check to make sure the game exists
        if(getGame(gameID) == null) throw new DataErrorException(500, "Error: gameID is invalid");

        var whiteUsername = game.getWhiteUsername();
        var blackUsername = game.getBlackUsername();
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, blackUsername);
            stmt.setString(2, whiteUsername);
            stmt.setInt(3, gameID);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }

    }


    /**
     * Deletes a game
     * @throws DataErrorException
     */
    @Override
    public void deleteGame() throws DataErrorException {
        var statement = "TRUNCATE TABLE Games";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when deleting game");
        }
    }


    /**
     * Deletes a game by gameID
     * @param gameName
     * @throws DataErrorException
     */
    @Override
    public boolean findGame(String gameName) throws DataErrorException {
        var statement = "SELECT * FROM Games WHERE gameName = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, gameName);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }
    }


    /**
     * Gets a game by gameID
     * @param gameID
     * @return
     * @throws DataErrorException
     */
    @Override
    public GameData getGame(int gameID) throws DataErrorException {
        var statement = "SELECT * FROM Games WHERE gameID = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setInt(1, gameID);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                var game = rs.getString("game");

                // serializing stuff
                Gson gson = new Gson();
                ChessGame gameObj = gson.fromJson(game, ChessGame.class);
                return new GameData(gameID, whiteUsername, blackUsername, gameName, gameObj);
            }
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when reading game");
        }
        return null;
    }


}
