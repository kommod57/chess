package dao;

public class UserDAO {
    private static final String valid_username = "user";
    private static final String valid_password = "passward5";

    public boolean saveUser(String username, String password) {
        return true;
    }

    public boolean validateUser(String username, String password) {
        return valid_username.equals(username) && valid_password.equals(password);
    }
}
