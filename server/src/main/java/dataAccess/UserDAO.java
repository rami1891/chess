package dataAccess;


import model.UserData;
import java.util.*;
public class UserDAO {
    public Collection<UserData> userData;



    /**
     * Creates a new user in the database
     * @param user
     * @throws DataAccessException
     */
    public void createUser(UserData user) throws DataAccessException{
        userData.add(user); //add user to the collection


    }


    public UserData readUser(String username) throws DataAccessException{
        return null;
    }


    public void updateUser(UserData user) throws DataAccessException {
    }

    public void deleteUser() throws DataAccessException {
        userData.clear();
    }


    public boolean findUser(String username) throws DataAccessException {
        for (UserData user : userData) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}