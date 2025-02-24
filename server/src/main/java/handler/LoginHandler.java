package handler;

import org.eclipse.jetty.util.log.Log;
import spark.Request;
import spark.Response;
import spark.Route;
import service.LoginService;

public class LoginHandler implements Route {
    private final LoginService loginService;

    public LoginHandler() {
        this.loginService = new LoginService();
    }
    @Override
    public Object handle(Request request, Response response) {
        // login
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        boolean loginSuccessful = Boolean.parseBoolean(loginService.login(username, password));

        if (loginSuccessful) {
            response.status(200);
            return "Success";
        } else {
            response.status(401);
        }
        return "Invalid username/password";
    }
}
