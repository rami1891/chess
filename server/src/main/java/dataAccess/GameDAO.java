package dataAccess;

import model.GameData;
import java.util.*;

public class GameDAO {
    public Collection<GameData> gameData;

    public GameDAO() {
        gameData = new ArrayList<GameData>();
    }


    public void createGame(GameData game) throws DataAccessException{
    }

    public Collection<GameData> listGames() throws DataAccessException{
        return gameData;
    }

    public void joinGame(GameData game) throws DataAccessException{
    }


    public void deleteGame() throws DataAccessException {
        gameData.clear();
    }




}
