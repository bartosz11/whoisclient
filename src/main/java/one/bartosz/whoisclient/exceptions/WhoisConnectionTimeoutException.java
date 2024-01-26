package one.bartosz.whoisclient.exceptions;


/**
 * An exception thrown when WHOIS server hostname got resolved, but the server couldn't be reached.
 */
public class WhoisConnectionTimeoutException extends WhoisIOException {

    public WhoisConnectionTimeoutException() {
        super();
    }

    public WhoisConnectionTimeoutException(String message) {
        super(message);
    }

    public WhoisConnectionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public WhoisConnectionTimeoutException(Throwable cause) {
        super(cause);
    }
}
