package handler;

import dao.UserDAO;
import spark.Request;
import spark.Response;
import spark.Route;
import service.LoginService;
import com.google.gson.Gson;
import server.models.LoginResponse;
import server.models.LoginRequest;
import java.sql.Connection;
import java.sql.DriverManager;

public class LoginHandler implements Route {
    private final Gson gson = new Gson();


    @Override
    public Object handle(Request request, Response response) {
        // login
        System.out.println("LoginHandler triggered");
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/chess_db", "username", "password")) {

            UserDAO userDAO = new UserDAO(connection);

            String requestBody = request.body();
            System.out.println("Received the body: " + requestBody);

            LoginService loginService = new LoginService(userDAO);

            LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);
            System.out.println("Login request: " + loginRequest);

            LoginResponse loginResponse = loginService.login(loginRequest);

            response.status(loginResponse.isSuccess() ? 200 : 401);

            return gson.toJson(loginResponse);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            response.status(400);
            response.type("application/json");
            return gson.toJson(new LoginResponse(false, "Request is not valid " + e.getMessage()));
        }

    }
}
