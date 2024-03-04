package dataAccess;


import model.UserData;
import java.util.*;
public interface UserDAO {

    /**
     * Creates a new user in the database
     * @param user
     * @throws DataAccessException
     */
    public void createUser(UserData user) throws DataAccessException;


    /**
     * Deletes all users in the database
     * @throws DataAccessException
     */
    public UserData readUser(String username) throws DataAccessException;



    /**
     * Deletes all users in the database
     * @throws DataAccessException
     */
    public void deleteUser() throws DataAccessException;



    /**
     * Deletes a specific user in the database
     * @param username
     * @throws DataAccessException
     */
    public boolean findUser(String username) throws DataAccessException;
}
