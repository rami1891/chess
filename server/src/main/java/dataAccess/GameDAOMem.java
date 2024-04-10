package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameDAOMem implements GameDAO{

    public Collection<GameData> gameData;

    public GameDAOMem() {
        gameData = new ArrayList<GameData>();
    }



    /**
     * Creates a new game in the database
     * @param game
     * @throws DataAccessException
     */
    public void createGame(GameData game) throws DataAccessException{
        gameData.add(game);
    }


    /**
     * Deletes all games in the database
     * @throws DataAccessException
     */
    public Collection<GameData> listGames() throws DataAccessException{
        return gameData;
    }


    /**
     * Deletes all games in the database
     * @throws DataAccessException
     */
    public void joinGame(GameData game) throws DataAccessException{
        gameData.add(game);
    }



    /**
     * Deletes all games in the database
     * @throws DataAccessException
     */
    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }


    /**
     * Deletes a specific game in the database
     * @param gameName
     * @throws DataAccessException
     */
    public boolean findGame(String gameName) throws DataAccessException{
        for(GameData game : gameData){
            if(game.getGameName().equals(gameName)){
                return true;
            }
        }
        return false;
    }



    /**
     * Deletes a specific game in the database
     * @param gameID
     * @throws DataAccessException
     */
    public GameData getGame(int gameID) throws DataAccessException{
        for(GameData game : gameData){
            if(game.getGameID() == gameID){
                return game;
            }
        }
        return null;
    }

    @Override
    public void gameOverride(GameData game) throws DataErrorException {
        for(GameData g : gameData){
            if(g.getGameID() == game.getGameID()){
                gameData.remove(g);
                gameData.add(game);
                return;
            }
        }
        throw new DataErrorException(500, "Error: gameID is invalid");
    }


}
