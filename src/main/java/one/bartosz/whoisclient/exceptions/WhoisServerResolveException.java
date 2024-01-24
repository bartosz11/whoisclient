package one.bartosz.whoisclient.exceptions;

/**
 * An exception thrown when resolving hostname of the WHOIS server fails.
 */
public class WhoisServerResolveException extends WhoisIOException { // I guess resolving exception is an I/O exception

    public WhoisServerResolveException() {
        super();
    }

    public WhoisServerResolveException(String message) {
        super(message);
    }

    public WhoisServerResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public WhoisServerResolveException(Throwable cause) {
        super(cause);
    }
}
