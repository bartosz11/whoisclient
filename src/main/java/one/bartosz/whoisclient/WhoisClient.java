package one.bartosz.whoisclient;

import one.bartosz.whoisclient.exceptions.WhoisConnectionTimeoutException;
import one.bartosz.whoisclient.exceptions.WhoisIOException;
import one.bartosz.whoisclient.exceptions.WhoisServerResolveException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that allows to query servers for information about resources using the WHOIS query protocol. Compliant with <a href="https://datatracker.ietf.org/doc/html/rfc3912">RFC 3912</a>.
 */
public class WhoisClient {

    private final WhoisClientConfig config;


    /**
     * Constructs a new WhoisClient instance with specified configuration.
     *
     * @param config Configuration to use in new WhoisClient instance. If null, default values will be used.
     * @see WhoisClientConfig
     */
    public WhoisClient(WhoisClientConfig config) {
        this.config = config != null ? config : new WhoisClientConfig();
    }


    /**
     * Queries the WhoisClient's instance default WHOIS server for information about specified resource.
     *
     * @param resource Resource/Request to send to the WHOIS server. Shouldn't have "\r\n" suffix.
     * @return WHOIS server response.
     * @throws WhoisServerResolveException Thrown when resolving hostname of default WHOIS server fails.
     * @throws WhoisIOException            Thrown when any other network I/O error occurs while querying the server.
     */
    public WhoisResponse query(String resource) throws WhoisServerResolveException, WhoisIOException {
        return query(resource, config.getDefaultHost());
    }

    /**
     * Queries the specified WHOIS server host with WhoisClient's instance default port for information about specified resource.
     *
     * @param resource Resource/Request to send to the WHOIS server. Shouldn't have "\r\n" suffix.
     * @param host     WHOIS server hostname
     * @return WHOIS server response
     * @throws WhoisServerResolveException Thrown when resolving hostname of default WHOIS server fails.
     * @throws WhoisIOException            Thrown when any other network I/O error occurs while querying the server.
     */
    public WhoisResponse query(String resource, String host) throws WhoisServerResolveException, WhoisIOException {
        return query(resource, host, config.getDefaultPort());
    }

    /**
     * Queries the specified WHOIS server host with specified port for information about specified resource.
     *
     * @param resource Resource/Request to send to the WHOIS server. Shouldn't have "\r\n" suffix.
     * @param host     WHOIS server hostname
     * @param port     WHOIS server port, must fit in 0-65535 inclusive range.
     * @return WHOIS server response
     * @throws WhoisServerResolveException     Thrown when resolving hostname of default WHOIS server fails.
     * @throws WhoisConnectionTimeoutException Thrown when the configured timeout expires before connecting to the server.
     * @throws WhoisIOException                Thrown when any other network I/O error occurs while querying the server.
     */
    public WhoisResponse query(String resource, String host, int port) throws WhoisServerResolveException, WhoisConnectionTimeoutException, WhoisIOException {
        ValidationUtil.validatePort(port);
        //todo maybe "sanitize" request later
        String request = resource + "\r\n";
        Charset charset = config.getCharset();
        Pattern matchingPattern = config.getMatchingPattern();
        HashMap<String, List<String>> responseFields = new HashMap<>();
        try {
            InetAddress ip = InetAddress.getByName(host);
            Socket socket = new Socket();
            //sadly there's no constructor in the Socket class that has a timeout param
            socket.connect(new InetSocketAddress(ip.getHostAddress(), port), config.getConnectTimeout());
            //Write the request
            socket.getOutputStream().write(request.getBytes(charset));
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            //We can safely use StringBuilder because WHOIS servers don't return any binary data
            StringBuilder sb = new StringBuilder();
            int readByte;
            //convert binary response to string
            while ((readByte = bis.read()) != -1) {
                sb.append((char) readByte);
            }
            //close the stuff - parsing is done
            bis.close();
            socket.close();
            String rawResponse = sb.toString();
            //turn the raw response into something usable
            Matcher matcher = matchingPattern.matcher(rawResponse);
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                //if the key exists - add value to it, if it doesn't - create it
                responseFields.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
            //make the responses field values readonly
            responseFields.forEach((k, v) -> responseFields.put(k, Collections.unmodifiableList(v)));
            //return a readonly response object
            return new WhoisResponse(resource, rawResponse, Collections.unmodifiableMap(responseFields), host);
        } catch (UnknownHostException e) {
            throw new WhoisServerResolveException("Failed to resolve WHOIS server hostname: " + host, e.getCause());
        } catch (SocketTimeoutException e) {
            throw new WhoisConnectionTimeoutException("Connection to " + host + " timed out after " + config.getConnectTimeout() + " ms.");
        } catch (IOException e) {
            throw new WhoisIOException("An I/O error occurred during request about " + resource + " from " + host, e.getCause());
        }
    }

    /**
     * Get WhoisClient's instance configuration.
     *
     * @return WhoisClient's instance configuration
     */
    public WhoisClientConfig getConfig() {
        return config;
    }
}