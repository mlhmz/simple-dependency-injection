package xyz.mlhmz.exceptions;

/**
 * Exception that can occur if the Injectable that should be fetched out of the InjectableContainer is not castable
 * to the Class that it is identified by.
 */
public class InjectableInvalidException extends RuntimeException {
}
