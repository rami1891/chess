package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static dataAccess.DatabaseManager.configureDatabase;

public class MySqlUserDAO implements UserDAO{

    /**
     * Constructor
     * @throws DataErrorException
     */
    public MySqlUserDAO() throws DataErrorException{
        configureDatabase();
    }


    /**
     * Creates a new user in the database
     * @param user
     * @throws DataErrorException
     */
    @Override
    public void createUser(UserData user) throws DataErrorException {
        var statement = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
        var username = user.getUsername();
        var password = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        var email = user.getEmail();
        executeStatement(statement, username, hashedPassword, email);
    }


    /**
     * Executes a statement
     * @param statement
     * @param username
     * @param password
     * @param email
     * @throws DataErrorException
     */
    private void executeStatement(String statement, String username, String password, String email) throws DataErrorException{
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(username != null)
                stmt.setString(1, username);
            if(password != null)
                stmt.setString(2, password);
            if(email != null)
                stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (Exception e) {

            throw new DataErrorException(500, e.getMessage());
        }
    }



    /**
     * Reads a user from the database
     * @param username
     * @return
     * @throws DataErrorException
     */
    @Override
    public UserData readUser(String username) throws DataErrorException {
        var statement = "SELECT * FROM Users WHERE username = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){

            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                var password = rs.getString("password");
                var email = rs.getString("email");
                return new UserData(username, password, email);
            }
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when reading user");
        }
        return null;
    }


    /**
     * Deletes a user from the database
     * @throws DataErrorException
     */
    @Override
    public void deleteUser() throws DataErrorException {
        var statement = "TRUNCATE TABLE Users";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(500, e.getMessage());
        }

    }

    /**
     * Updates a user in the database
     * @param username
     * @throws DataErrorException
     */
    @Override
    public boolean findUser(String username) throws DataErrorException {
        var statement = "SELECT * FROM Users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, username);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }
    }
}
