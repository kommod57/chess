package service;

import dao.UserDAO;
import org.eclipse.jetty.util.log.Log;

public class LoginService {

    private final UserDAO userDAO;
    public LoginService() {
        this.userDAO = new UserDAO();
    }

    public boolean login(String username, String password) {
        return userDAO.validateUser(username, password);
    }
}
