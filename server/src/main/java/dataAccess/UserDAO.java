package dataAccess;


import model.UserData;
import java.util.*;
public class UserDAO {
    public Collection<UserData> userData;

    public UserDAO() {
        userData = new ArrayList<UserData>();
    }



    /**
     * Creates a new user in the database
     * @param user
     * @throws DataAccessException
     */
    public void createUser(UserData user) throws DataAccessException{
        userData.add(user); //add user to the collection


    }

    public UserData readUser(String username) throws DataAccessException{
         for (UserData user : userData) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
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
