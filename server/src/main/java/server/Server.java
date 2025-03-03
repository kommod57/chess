package server;

import com.google.gson.Gson;
import handler.LoginHandler;
import spark.Spark;
import spark.routematch.RouteMatch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class Server {

    public int run(int desiredPort) throws ClassNotFoundException {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        System.out.println("Server on port " + desiredPort);

        Gson gson = new Gson();
        System.out.println("Accessed server");

        Spark.get("/", (request, response) -> {
            System.out.println(("Root route accessed"));
            return  "Server is running";
                });


        System.out.println("Registering /login route...");
        Spark.post("/login", new LoginHandler(), gson::toJson);

        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body("Server Error: " + exception.getMessage());
        });
        System.out.println("Mapped Routes: ");
        for (RouteMatch route : Spark.routes()) {
            System.out.println("Route: " + route.getMatchUri() + " | Method: " + route.getHttpMethod());
        }


        Spark.get("/session", (req, res) -> {
            return "Session is not implemented.";
        });

        Spark.awaitInitialization();


        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


}

