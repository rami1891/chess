package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class AuthDAOMem implements AuthDAO {

    public Collection<AuthData> authData;

    public AuthDAOMem() {
        authData = new ArrayList<AuthData>();
    }


    /**
     * Creates a new auth in the database
     * @param auth
     * @throws DataAccessException
     */
    @Override
    public void createAuth(AuthData auth) throws DataAccessException{
        authData.add(auth); //add auth to the collection
    }

    /**
     * Deletes all auths in the database
     * @throws DataAccessException
     */
    @Override

    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }

    /**
     * Deletes a specific auth in the database
     * @param authToken
     * @throws DataAccessException
     */
    @Override

    public void deleteMyAuth(String authToken) throws DataAccessException, DataErrorException {
        for (AuthData auth : authData) {
            if (auth.getAuthToken().equals(authToken)) {
                authData.remove(auth);

            }
        }
        throw new DataErrorException(401, "Error: Unauthorized");
    }


    /**
     * Finds a specific auth in the database
     * @param authtoken
     * @return
     * @throws DataAccessException
     */
    @Override

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
    @Override

    public AuthData getAuth(String authtoken) throws DataAccessException{
        for(AuthData auth : authData){
            if(auth.getAuthToken().equals(authtoken)){
                return auth;
            }
        }
        return null;
    }
}
