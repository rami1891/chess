package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.*;


public class AuthDAO {
    public Collection<AuthData> authData;

    public AuthDAO() {
        authData = new ArrayList<AuthData>();
    }


    /**
     * Creates a new auth in the database
     * @param auth
     * @throws DataAccessException
     */
    public void createAuth(AuthData auth) throws DataAccessException{
        authData.add(auth); //add auth to the collection
    }

    /**
     * Deletes all auths in the database
     * @throws DataAccessException
     */
    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }

    /**
     * Deletes a specific auth in the database
     * @param authToken
     * @throws DataAccessException
     */
    public AuthData deleteMyAuth(String authToken) throws DataAccessException {
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                authData.remove(auth);
                return auth;
            }
        }
        return null;
    }


    /**
     * Finds a specific auth in the database
     * @param authtoken
     * @return
     * @throws DataAccessException
     */
    public boolean findAuth(String authtoken) throws DataAccessException{
        for(AuthData auth : authData){
            if(auth.getAuthToken().equals(authtoken)){
                return true;
            }
        }
        return false;
    }


    /**
     * Gets a specific auth in the database
     * @param authtoken
     * @return
     * @throws DataAccessException
     */
    public AuthData getAuth(String authtoken) throws DataAccessException{
        for(AuthData auth : authData){
            if(auth.getAuthToken().equals(authtoken)){
                return auth;
            }
        }
        return null;
    }
}
