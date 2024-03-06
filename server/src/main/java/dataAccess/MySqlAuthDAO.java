package dataAccess;

import model.AuthData;

import static dataAccess.DatabaseManager.configureDatabase;

public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO() throws DataErrorException {
        configureDatabase();
    }
    @Override
    public void createAuth(AuthData auth) throws DataErrorException {
        var statement = "INSERT INTO Auth (authToken, username) VALUES (?, ?)";
        var authToken = auth.getAuthToken();
        var username = auth.getUsername();
        executeStatement(statement, authToken, username);



    }

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

    @Override
    public void deleteAuth() throws DataErrorException {
        var statement = "TRUNCATE TABLE Auth";
        executeStatement(statement, null, null);


    }

    @Override
    public void deleteMyAuth(String authToken) throws DataErrorException {
        if(findAuth(authToken) == false)
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
