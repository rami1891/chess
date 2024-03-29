package dataAccess;

import model.AuthData;

import static dataAccess.DatabaseManager.configureDatabase;

public class MySqlAuthDAO implements AuthDAO{


    /**
     * Constructor
     * @throws DataErrorException
     */
    public MySqlAuthDAO() throws DataErrorException {
        configureDatabase();
    }

    /**
     * Creates a new auth token in the database
     * @param auth
     * @throws DataErrorException
     */
    @Override
    public void createAuth(AuthData auth) throws DataErrorException {
        var statement = "INSERT INTO Auth (authToken, username) VALUES (?, ?)";
        var authToken = auth.getAuthToken();
        var username = auth.getUsername();
        executeStatement(statement, authToken, username);

    }


    /**
     * Executes a statement
     * @param statement
     * @param authToken
     * @param username
     * @throws DataErrorException
     */
    private void executeStatement(String statement, String authToken, String username) throws DataErrorException{
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(authToken != null)
                stmt.setString(1, authToken);
            if(username != null)
                stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }
    }



    /**
     * Deletes all auth tokens in the database
     * @throws DataErrorException
     */
    @Override
    public void deleteAuth() throws DataErrorException {
        var statement = "TRUNCATE TABLE Auth";
        executeStatement(statement, null, null);
    }


    /**
     * Deletes a specific auth token in the database
     * @param authToken
     * @throws DataErrorException
     */
    @Override
    public void deleteMyAuth(String authToken) throws DataErrorException {
        if(!findAuth(authToken))
            throw new DataErrorException(401, "Error: Unauthorized");

        var statement = "DELETE FROM Auth WHERE authToken = ?";
        //executeStatement(statement, authToken, null);
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            if(authToken != null)
                stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new DataErrorException(401, "Error: Unauthorized " + statement);
        }


    }



    /**
     * Finds an auth token in the database
     * @param authToken
     * @return
     * @throws DataErrorException
     */
    @Override
    public boolean findAuth(String authToken) throws DataErrorException{
        var statement = "SELECT * FROM Auth WHERE authToken = ?";
        try(var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, authToken);
            var rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }
    }


    /**
     * Gets an auth token in the database
     * @param authtoken
     * @return
     * @throws DataErrorException
     */
    @Override
    public AuthData getAuth(String authtoken) throws DataErrorException {
        var statement = "SELECT * FROM Auth WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection(); var stmt = conn.prepareStatement(statement)){
            stmt.setString(1, authtoken);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                var authToken = rs.getString("authToken");
                var username = rs.getString("username");
                AuthData auth = new AuthData();
                auth.setAuthToken(authToken);
                auth.setUsername(username);
                return auth;
            }
            return null;
        } catch (Exception e) {
            throw new DataErrorException(500, "Error when executing statement: " + statement);
        }
    }

}
