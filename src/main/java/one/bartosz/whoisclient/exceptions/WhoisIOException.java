package one.bartosz.whoisclient.exceptions;

import java.io.IOException;

/**
 * An exception thrown when an I/O error occurs during a WHOIS query.
 */
public class WhoisIOException extends IOException {

    public WhoisIOException() {
        super();
    }

    public WhoisIOException(String message) {
        super(message);
    }

    public WhoisIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public WhoisIOException(Throwable cause) {
        super(cause);
    }
}
