package server;

import spark.*;
import server.handler.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.delete("/db", new ClearHandler());
        Spark.post("/user", new RegisterHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.get("/game", new ListGamesHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
