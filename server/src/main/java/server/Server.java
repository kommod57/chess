package server;

import spark.*;
import server.handler.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.delete("/db", (Route) new ClearHandler());
        Spark.post("/user", (Route) new RegisterHandler());
        Spark.post("/session", (Route) new LoginHandler());
        Spark.delete("/session", (Route) new LogoutHandler());
        Spark.get("/game", (Route) new ListGamesHandler());
        Spark.post("/game", (Route) new CreateGameHandler());
        Spark.put("/game", (Route) new JoinGameHandler());

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
