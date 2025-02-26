package server.models;

public class LoginRequest {
    private String username;
    private String password;

//    // Gson to JSON
//    public LoginRequest() {}
//
//    public LoginRequest(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.username = password;
    }
}

