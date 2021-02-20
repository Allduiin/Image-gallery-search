package imagegallerysearch.search.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, RuntimeException e) {
        super(message, e);
    }
}
