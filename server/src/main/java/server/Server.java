package server;

import service.LoginService;
import spark.Spark;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/", (request, response) -> {
            response.type("text/html");
            return "<html><body><h1>Chess Server</h1><body></html>";
        });

        Spark.post("/login", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            // Call login service for validation
            LoginService loginService = new LoginService();
            boolean loginSuccessful = Boolean.parseBoolean(loginService.login(username, password));

            if (loginSuccessful) {
                response.status(200); // is ok
                return "Log in successful";
            } else {
                return "Username or password is incorrect";
            }
        });

        Spark.exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body("Server Error: " + exception.getMessage());
        });
//        //This line initializes the server and can be removed once you have a functioning endpoint
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

