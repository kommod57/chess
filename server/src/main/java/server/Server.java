package server;

import com.google.gson.Gson;
import handler.LoginHandler;
import org.eclipse.jetty.util.log.Log;
import service.LoginService;
import spark.Spark;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        System.out.println("Server on port " + desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
//        Spark.get("/", (request, response) -> {
//            response.type("text/html");
//            return "<html><body><h1>Chess Server</h1><body></html>";
//        });
        Gson gson = new Gson();
        System.out.println("Doing a login?");

        Spark.get("/", (request, response) -> {
            System.out.println(("Root route accessed"));
            return  "Server is running";
                });

        Spark.post("/login", new LoginHandler(), gson::toJson);

        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body("Server Error: " + exception.getMessage());
        });
//        //This line initializes the server and can be removed once you have a functioning endpoint
//        Spark.init();

        Spark.init();
        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

