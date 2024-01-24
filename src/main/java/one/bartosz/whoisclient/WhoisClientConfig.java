package one.bartosz.whoisclient;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Class allowing to configure a {@link WhoisClient} instance.
 * Configurable properties include:
 * - Charset used for binary to string conversion, default is UTF-8.
 * - Pattern used for extracting keys and values from WHOIS responses. Default is ^\s*([\w /\-]+): +(.+?)$
 * - Default WHOIS server host. Default value is "whois.iana.org".  Can be overridden per-request.
 * - Default WHOIS server port. Default value is 43, as stated in the <a href="https://datatracker.ietf.org/doc/html/rfc3912">RFC 3912</a>. Can be overridden per-request.
 */
public class WhoisClientConfig {

    //I copied this pattern from https://github.com/bestchanges/whoisclient/blob/6e74cc788c0e86ab94f9512be8205f56d820ebf6/java/src/main/java/io/github/bestchanges/whoisclient/WhoisRecord.java#L34
    //It's good enough to serve as default, user can modify it if they want
    private Pattern matchingPattern = Pattern.compile("^\\s*([\\w /\\-]+): +(.+?)$", Pattern.MULTILINE);
    private Charset charset = StandardCharsets.UTF_8;
    //The most common WHOIS use case is domain info lookup - that's why IANA's server is there - not a big deal anyway since it can be changed at request level too
    private String defaultHost = "whois.iana.org";
    //RFC states that WHOIS servers should listen on port 43 TCP
    private int defaultPort = 43;

    /**
     * Returns the pattern used for extracting keys and values from WHOIS responses. Default is "^\s*([\w /\-]+): +(.+?)$"
     * @return Pattern used for extracting keys and values from WHOIS responses
     */
    public Pattern getMatchingPattern() {
        return matchingPattern;
    }

    /**
     * Validates and sets the specified pattern to be used for extracting keys and values from WHOIS responses.
     * @param pattern String representation of the regex pattern to set and validate.
     * @throws IllegalArgumentException Thrown if the provided pattern doesn't meet one or more of the following conditions:
     *                                  - Must start with a "^",
     *                                  - Must end with a "$",
     *                                  - Must have exactly two capture groups.
     * @return This instance of WhoisClientConfig, allowing for setter method chaining.
     */
    public WhoisClientConfig setMatchingPattern(String pattern) {
        this.matchingPattern = ValidationUtil.validateAndCompilePattern(pattern);
        return this;
    }

    /**
     * Returns the charset used for binary to string WHOIS response conversions. Default is UTF-8.
     * @return Charset used for binary to string WHOIS response conversions.
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Sets the charset to use for binary to string WHOIS response conversions.
     * @param charset Charset to use
     * @return This instance of WhoisClientConfig, allowing for setter method chaining.
     */
    public WhoisClientConfig setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Returns the hostname of default WHOIS server. Default value is "whois.iana.org".
     * @return Hostname of the default WHOIS server
     */
    public String getDefaultHost() {
        return defaultHost;
    }

    /**
     * Sets the default WHOIS server hostname.
     * @param defaultHost Default server hostname to use
     * @return This instance of WhoisClientConfig, allowing for setter method chaining.
     */
    public WhoisClientConfig setDefaultHost(String defaultHost) {
        this.defaultHost = defaultHost;
        return this;
    }

    /**
     * Returns the default port to connect to WHOIS servers at. Default value is 43, as stated in <a href="https://datatracker.ietf.org/doc/html/rfc3912">RFC 3912</a>.
     * @return Default port to connect to WHOIS server at
     */
    public int getDefaultPort() {
        return defaultPort;
    }

    /**
     * Sets the default port to connect to WHOIS servers at.
     * @param defaultPort Default port to use, must fit in 0-65535 inclusive range.
     * @return This instance of WhoisClientConfig, allowing for setter method chaining.
     */
    public WhoisClientConfig setDefaultPort(int defaultPort) {
        this.defaultPort = ValidationUtil.validatePort(defaultPort);
        return this;
    }
}
