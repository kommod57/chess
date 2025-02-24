package handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
        // login
        return "Login request successful.";
    }
}
