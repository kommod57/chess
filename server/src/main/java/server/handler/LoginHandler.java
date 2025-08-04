package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.security.LoginService;
import spark.Request;
import spark.Response;
import model.request.LoginRequest.LoginRequest;
import model.response.LoginResponse;


public class LoginHandler {
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        LoginResponse response = new LoginService().login(request);
        return gson.toJson(response);
    }
}
