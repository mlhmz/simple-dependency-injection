package xyz.mlhmz.exceptions;

/**
 * Exception that can occur if the initialization of an injectable class failed.
 */
public class InjectableInitializationFailed extends RuntimeException {
    public InjectableInitializationFailed(String message) {
        super(message);
    }
}
