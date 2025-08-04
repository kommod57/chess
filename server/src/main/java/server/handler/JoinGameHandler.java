package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.security.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;
import service.LoginService;
import model.request.LoginRequest;
import model.response.LoginResponse;


public class JoinGameHandler {
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameResponse response = new JoinGameService().login(request);
        return gson.toJson(response);
    }
}