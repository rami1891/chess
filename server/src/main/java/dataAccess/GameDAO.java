package dataAccess;

import model.GameData;
import java.util.*;

public class GameDAO {
    public Collection<GameData> gameData;

    public GameDAO() {
        gameData = new ArrayList<GameData>();
    }


    public void createGame(GameData game) throws DataAccessException{
        gameData.add(game);
    }

    public Collection<GameData> listGames() throws DataAccessException{
        return gameData;
    }

    public void joinGame(GameData game) throws DataAccessException{
    }


    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }

    public boolean findGame(String gameName) throws DataAccessException{
        for(GameData game : gameData){
            if(game.getGameName().equals(gameName)){
                return true;
            }
        }
        return false;
    }



    public GameData getGame(int gameID) throws DataAccessException{
        for(GameData game : gameData){
            if(game.getGameID() == gameID){
                return game;
            }
        }
        return null;
    }






}
