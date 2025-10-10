package server.handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import model.request.RegisterRequest;
import model.response.RegisterResponse;


public class RegisterHandler implements spark.Route{
    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResponse response = new RegisterService().login(request);
        return gson.toJson(response);
    }
}
