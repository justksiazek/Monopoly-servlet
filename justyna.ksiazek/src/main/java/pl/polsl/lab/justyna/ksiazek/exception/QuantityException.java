package pl.polsl.lab.justyna.ksiazek.exception;

/**
 * Exception of players quantity.
 * 
 * @author Justyna Ksiazek
 * @version 1.0
 * @since 1.0
 */
public class QuantityException extends Exception {
    /** Non-parameter constructor */
    public QuantityException() { }

    /** Exception class constructor
     * @param message display message
     */
    public QuantityException(String message) {
        super(message);
    }
}