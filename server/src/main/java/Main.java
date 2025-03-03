import chess.*;
import model.Game;
import service.LoginService;
import service.RegisterService;
import spark.Spark;
import service.DatabaseService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        // Clear database
        Spark.delete("/db", (request, response) -> {
            try {
                DatabaseService.clearDatabase();
                response.status(200);
                return "{}";
            } catch (SQLException e) {
                response.status(500);
                return "Error while clearing database: " + e.getMessage();
            }
        });

        // Register User Endpoint
//        Spark.post("/user", ((request, response) -> {
//            String username = request.queryParams("username");
//            String paswsword = request.queryParams("password");
//            String email = request.queryParams("email");
//
//            if (RegisterService.)
//        }));

        // Login endpoint
        Spark.post("/session", ((request, response) -> {
            String username = request.queryParams("username");
            String paswsword = request.queryParams("password");

            try {
                if (DatabaseService.authenticateUser(username, paswsword)) {
                    String authToken = "my auth token";
                    response.status(200);
                    return "{\"username\": \"" + username + "\", \"authToken\": \"" + authToken + "\"}";
                } else {
                    response.status(401);
                    return "Unauthorized";
                }
            } catch (SQLException e) {
                response.status(500);
                return e.getMessage();
            }
        }));

        // logout User Endpoint
        Spark.delete("/session", ((request, response) -> {
            String authToken = request.headers("authorization");

            response.status(200);
            return "{}";
        }));

        // list game endpoint
//        Spark.get("/game", ((request, response) -> {
//            String authToken = request.headers("authorization");
//
//            try {
//                List<Map<String, Object>> games = GameService
//            }
//        }
//        ));

        // create game endpoint

        // join game endpoint
//        Spark.put("/game", ((request, response) -> {
//            String authToken = request.headers("authorization");
//            int gameID = Integer.parseInt(request.queryParams("gameID"));
//            String playerColor = request.queryParams("playerColor");
//
//            try {
//                if (DatabaseService.joingame)
//            }
//        }))

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        server.Server server = new server.Server();
        server.run(5051);
    }
}