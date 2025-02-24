package util;

public class Validator {
    public static boolean isValidUsername(String username) {
        return username.length() >= 4;
    }
}
