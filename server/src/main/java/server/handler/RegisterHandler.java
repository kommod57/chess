package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.security.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;
import service.LoginService;
import model.request.LoginRequest;
import model.response.LoginResponse;


public class RegisterHandler {
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResponse response = new RegisterService().login(request);
        return gson.toJson(response);
    }
}
