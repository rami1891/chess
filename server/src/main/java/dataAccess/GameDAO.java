package dataAccess;

import model.GameData;
import java.util.*;

public class GameDAO {
    public Collection<GameData> gameData;


    public void createGame(GameData game) throws DataAccessException{
    }

    public Collection<GameData> listGames() throws DataAccessException{
        return null;
    }

    public void joinGame(GameData game) throws DataAccessException{
    }


    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }


}
