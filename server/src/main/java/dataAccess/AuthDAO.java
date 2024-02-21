package dataAccess;

import model.AuthData;
import java.util.*;


public class AuthDAO {
    public Collection<AuthData> authData;

    public void createAuth(AuthData auth) throws DataAccessException{
        authData.add(auth); //add auth to the collection
    }

    public Collection<AuthData> listAuths() throws DataAccessException{
        return null;
    }

    public void deleteAuth() throws DataAccessException {
        authData.clear();
    }
}
