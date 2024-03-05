package dataAccess;

import model.GameData;
import java.util.*;

public interface GameDAO {


    /**
     * Creates a new game in the database
     * @param game
     * @throws DataAccessException
     */
    public void createGame(GameData game) throws DataAccessException, DataErrorException;


    /**
     * Deletes all games in the database
     * @throws DataAccessException
     */
    public Collection<GameData> listGames() throws DataAccessException, DataErrorException;


    /**
     * Deletes all games in the database
     * @throws DataAccessException
     */
    public void joinGame(GameData game) throws DataAccessException, DataErrorException;



    /**
     * Deletes all games in the database
     * @throws DataAccessException
     */
    public void deleteGame() throws DataAccessException, DataErrorException;


    /**
     * Deletes a specific game in the database
     * @param gameName
     * @throws DataAccessException
     */
    public boolean findGame(String gameName) throws DataAccessException, DataErrorException;



    /**
     * Deletes a specific game in the database
     * @param gameID
     * @throws DataAccessException
     */
    public GameData getGame(int gameID) throws DataAccessException, DataErrorException;

}
