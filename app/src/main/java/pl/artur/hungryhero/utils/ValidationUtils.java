package pl.artur.hungryhero.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            return false;
        }

        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{6,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean arePasswordsMatching(String password, String repeatPassword) {
        return password.equals(repeatPassword);
    }

}
