package services;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public class registrationService {
    public registrationService() {
    }

    public void createUser(UserData user) {
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        if(user.getUsername() == null || user.getUsername().equals("")){
            throw new IllegalArgumentException("User must have a username");
        }
        if(user.getPassword() == null || user.getPassword().equals("")){
            throw new IllegalArgumentException("User must have a password");
        }
        if(user.getEmail() == null || user.getEmail().equals("")){
            throw new IllegalArgumentException("User must have an email");
        }










        throw new UnsupportedOperationException("Not supported yet.");
    }




}
