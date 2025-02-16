package bg.sofia.uni.fmi.mjt.todoist.server.user;

import bg.sofia.uni.fmi.mjt.todoist.server.exception.commandexception.PasswordFormatException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordManager {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 25;
    private static final int HASH_MASK = 0xff;
    private static final MessageDigest MESSAGE_DIGEST;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("An unexpected error has occurred while hashing with SHA256", e);
        }
    }

    public static void validate(String password) throws PasswordFormatException {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (password.length() < MIN_LENGTH) {
            throw new PasswordFormatException("Password must be at least " + MIN_LENGTH + " symbols");
        }

        if (password.length() > MAX_LENGTH) {
            throw new PasswordFormatException("Password must be at most " + MAX_LENGTH + " symbols");
        }

        if (password.equals(password.toLowerCase())) {
            throw new PasswordFormatException("Password must have at least 1 upper-case symbol");
        }

        if (password.equals(password.toUpperCase())) {
            throw new PasswordFormatException("Password must have at least 1 lower-case symbol");
        }

        if (!hasDigit(password)) {
            throw new PasswordFormatException("Password must have at least 1 digit");
        }
    }

    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Cannot hash a null password");
        }

        MESSAGE_DIGEST.update(password.getBytes());
        byte[] digest = MESSAGE_DIGEST.digest();

        StringBuilder hashResult = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(HASH_MASK & b);
            if (hex.length() == 1) {
                hashResult.append('0');
            }
            hashResult.append(hex);
        }

        return hashResult.toString();
    }

    private static boolean hasDigit(String password) {
        boolean hasDigit = false;
        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }

        return hasDigit;
    }
}