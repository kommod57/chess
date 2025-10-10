package server.handler;

import com.google.gson.Gson;
import service.JoinGameService;
import spark.Request;
import spark.Response;
import model.request.JoinGameRequest;
import model.response.JoinGameResponse;


public class JoinGameHandler implements spark.Route{
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        JoinGameResponse response = new JoinGameService().login(request);
        return gson.toJson(response);
    }
}