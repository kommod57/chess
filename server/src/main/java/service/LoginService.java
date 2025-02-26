package service;

import dao.UserDAO;
import server.models.LoginRequest;
import server.models.LoginResponse;
import model.User;

import java.util.Objects;


public class LoginService {

    private UserDAO userDAO;

    public LoginService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userDAO.findUserByUsername(loginRequest.getUsername());

        if (user == null) {
            return new LoginResponse(false, "Username or password is not valid");
        }

        if (Objects.equals(loginRequest.getPassword(), user.getPassword())) {
            return new LoginResponse(true, "Success!");
        }
        return new LoginResponse(false, "Username or password is incorrect");
    }
}
