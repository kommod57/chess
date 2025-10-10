package service;

import dataAccess.UserDAO;
import model.request.LoginRequest.LoginRequest;
import model.response.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest request) {
        return new LoginResponse();
    }
}
