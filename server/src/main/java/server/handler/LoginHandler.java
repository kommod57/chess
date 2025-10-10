package server.handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import service.LoginService;
import model.request.LoginRequest;
import model.response.LoginResponse;



public class LoginHandler implements spark.Route{
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        LoginResponse response = new LoginService().login(request);
        return gson.toJson(response);
    }
}
