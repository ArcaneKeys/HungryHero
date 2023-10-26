package pl.artur.hungryhero.utils;

import android.util.Patterns;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

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

    public static boolean isValidPhoneNumber(String phoneNumber) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "PL");
            return phoneUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return false;
    }

}
