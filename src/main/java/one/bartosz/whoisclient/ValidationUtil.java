package one.bartosz.whoisclient;

import java.util.regex.Pattern;


/**
 * Class containing validation methods for library's internal use.
 */
public class ValidationUtil {

    /**
     * Checks if an int fits within 0-65535 inclusive range.
     *
     * @param port Port to validate
     * @return Validated port
     * @throws IllegalArgumentException Thrown if the port doesn't fit in 0-65535, inclusive range.
     */
    public static int validatePort(int port) {
        if (!(port >= 0 && port <= 65535))
            throw new IllegalArgumentException("Port has to fit in range 0-65535, inclusive.");
        return port;
    }


    /**
     * Validates and compiles (if valid) a regex pattern for parsing WHOIS responses.
     *
     * @param pattern String representation of the regex pattern.
     *                Must start with a "^" (caret) and must end with "$" (dollar sign).
     *                Must have exactly 2 capture groups for key and value extraction.
     * @return Compiled Pattern instance ready for usage by WhoisClient.
     * @throws IllegalArgumentException Thrown if the provided pattern doesn't meet one or more conditions:
     *                                  - Must start with a "^",
     *                                  - Must end with a "$",
     *                                  - Must have exactly two capture groups.
     */
    public static Pattern validateAndCompilePattern(String pattern) {
        if (!pattern.startsWith("^")) throw new IllegalArgumentException("Pattern string has to start with a ^");
        if (!pattern.endsWith("$")) throw new IllegalArgumentException("Pattern string has to end with a $");
        Pattern compiled = Pattern.compile(pattern, Pattern.MULTILINE);
        if (compiled.matcher("").groupCount() != 2)
            throw new IllegalArgumentException("Pattern needs to have two capturing groups: one for key and one for value.");
        return compiled;
    }

}
