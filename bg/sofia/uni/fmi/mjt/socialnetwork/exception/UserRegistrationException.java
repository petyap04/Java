package bg.sofia.uni.fmi.mjt.socialnetwork.exception;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException() {
        super("User has been already registered");
    }
}