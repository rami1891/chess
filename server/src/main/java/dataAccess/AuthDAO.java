package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.*;


public interface AuthDAO {



    /**
     * Creates a new auth in the database
     * @param auth
     * @throws DataAccessException
     */
    public void createAuth(AuthData auth) throws DataAccessException, DataErrorException;

    /**
     * Deletes all auths in the database
     * @throws DataAccessException
     */
    public void deleteAuth() throws DataAccessException, DataErrorException;

    /**
     * Deletes a specific auth in the database
     * @param authToken
     * @throws DataAccessException
     */
    public AuthData deleteMyAuth(String authToken) throws DataAccessException, DataErrorException;


    /**
     * Finds a specific auth in the database
     * @param authtoken
     * @return
     * @throws DataAccessException
     */
    public boolean findAuth(String authtoken) throws DataAccessException, DataErrorException;


    /**
     * Gets a specific auth in the database
     * @param authtoken
     * @return
     * @throws DataAccessException
     */
    public AuthData getAuth(String authtoken) throws DataAccessException, DataErrorException;
}
