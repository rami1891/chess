package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.*;


public class AuthDAO {
    public Collection<AuthData> authData;

    public AuthDAO() {
        authData = new ArrayList<AuthData>();
    }

    public void createAuth(AuthData auth) throws DataAccessException{
        authData.add(auth); //add auth to the collection
    }

    public Collection<AuthData> listAuths() throws DataAccessException{
        return null;
    }

    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }

    public AuthData deleteMyAuth(String username) throws DataAccessException {
        for (AuthData auth : authData) {
            if (auth.getUsername().equals(username)) {
                authData.remove(auth);
                return auth;
            }
        }
        return null;
    }

    public boolean findAuth(String authtoken) throws DataAccessException{
        for(AuthData auth : authData){
            if(auth.getAuthToken().equals(authtoken)){
                return true;
            }
        }
        return false;
    }

    public AuthData getAuth(String authtoken) throws DataAccessException{
        for(AuthData auth : authData){
            if(auth.getAuthToken().equals(authtoken)){
                return auth;
            }
        }
        return null;
    }
}
