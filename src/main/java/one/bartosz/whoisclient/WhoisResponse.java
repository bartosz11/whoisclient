package one.bartosz.whoisclient;

import java.util.List;
import java.util.Map;

/**
 * Class representing a response of a WHOIS server.
 */
public class WhoisResponse {
    private final String requestedResource;
    private final String rawResponse;
    private final Map<String, List<String>> fields;
    private final String whoisServerHostname;

    public WhoisResponse(String requestedResource, String rawResponse, Map<String, List<String>> fields, String whoisHost) {
        this.requestedResource = requestedResource;
        this.rawResponse = rawResponse;
        this.fields = fields;
        this.whoisServerHostname = whoisHost;
    }

    /**
     * Returns the resource that the WHOIS request was made for.
     * @return The resource that the WHOIS request was made for
     */
    public String getRequestedResource() {
        return requestedResource;
    }

    /**
     * Returns a string representation of this WHOIS response. Returned value is equal to server's response.
     * @return Server's raw response for the WHOIS request
     */
    public String getRawResponse() {
        return rawResponse;
    }

    /**
     * Returns a map representation of WHOIS server's response
     * @return An unmodifiable map representation of WHOIS server's response.
     *         Since keys in WHOIS responses don't have to be unique returned map maps lists of values to a single key, for example:
     *         <pre>{@code
     *          Name Server: ns1.example.com
     *          Name Server: ns2.example.com
     *          Name Server: ns3.example.com
     *         }</pre>
     *         Returned by a WHOIS server will be mapped using:
     *         <pre>{@code
     *          map.put("Name Server", List.of("ns1.example.com", "ns2.example.com", "ns3.example.com"));
     *         }</pre>
     */
    public Map<String, List<String>> getFields() {
        return fields;
    }

    /**
     * Returns the hostname of the WHOIS server that sent the response.
     * @return The hostname of the WHOIS server that sent the response
     */
    public String getWhoisServerHostname() {
        return whoisServerHostname;
    }
}
