package pl.artur.hungryhero.utils;

import java.util.Random;

public class UsernameGenerator {
    private static final int USERNAME_LENGTH = 8;
    private static final int RANDOM_NUMBER_BOUND = 100000000;

    public static String generateUsername() {
        Random random = new Random();
        int randomNumber = random.nextInt(RANDOM_NUMBER_BOUND);
        String paddedNumber = String.format("%0" + USERNAME_LENGTH + "d", randomNumber);
        return "user@" + paddedNumber;
    }
}
