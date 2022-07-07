/*
 * Copyright 2017 Sitemorse (UK Sales) Ltd All rights reserved.
 *
 */

package uk.nhs.digital.sitemorse.sci;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLSocketFactory;


/**
 * Implement the Sitemorse CMS Integration (SCI) Client protocol.
 *
 * @author Sitemorse (UK Sales) Ltd
 * @version 1.2
 */
public class SciClient {
    /**
     * The default hostname of the SCI server.
     */
    private static final String SCI_DEFAULT_SERVER = "sci.sitemorse.com";
    /**
     * The default TCP port to connect to on the SCI server, when not using SSL.
     */
    private static final int SCI_DEFAULT_PORT = 5371;
    /**
     * The default TCP port to connect to on the SCI server, when using SSL.
     */
    private static final int SCI_DEFAULT_SSL_PORT = 5372;
    /**
     * The default TCP port for an HTTP proxy server.
     */
    private static final int HTTP_PROXY_PORT = 3128;
    /**
     * The time-out when connecting to the SCI server, in milliseconds.
     */
    private static final int SCI_CONNECT_TIMEOUT = 30 * 1000;
    /**
     * The time-out when reading from the SCI server, in milliseconds.
     */
    private static final int SCI_READ_TIMEOUT = 240 * 1000;
    /**
     * The time-out for performing the web request, in milliseconds.
     */
    private static final int WEB_TIMEOUT = 60 * 1000;
    /**
     * The buffer size to use when reading HTTP request/response bodies. This
     * needs to be small in order for the WEB_TIMEOUT to work properly.
     */
    private static final int BUFFER_SIZE = 512;
    /**
     * Lines are ended by carriage-return then line-feed (0x0d 0x0a).
     */
    private static final String CRLF = "\r\n";
    /**
     * We use ISO-8859-1 when reading and writing from sockets. This is because
     * it is an 8-bit single-byte character set and is a superset of US-ASCII,
     * but we don't actually ever treat top-bit-set bytes as any other than
     * opaque binary data.
     */
    private static final String SCI_CHARSET = "ISO-8859-1";
    /**
     * The Java identifier string for the HMAC-SHA1 algorithm that is used by
     * the SCI protocol authentication stage.
     */
    private static final String SCI_HASH_ALGORITHM = "HmacSHA1";

    /**
     * The host name of the SCI server.
     */
    private String serverHostname = null;
    /**
     * The TCP port number of the SCI server.
     */
    private int serverPort = 0;
    /**
     * Whether or not to use SSL to connect to the SCI server.
     */
    private boolean serverSecure = true;
    /**
     * Whether or not we should allow HTTP POST requests to be proxied through
     * us.
     */
    private boolean postAllowed = false;
    /**
     * The configured SCI licence key.
     */
    private String licenceKey = null;
    /**
     * The hostname of the HTTP proxy server to tunnel the SCI connection
     * through.
     */
    private String sciProxyHostname = null;
    /**
     * The port number of the HTTP proxy server to tunnel the SCI connection
     * through.
     */
    private int sciProxyPort = 0;
    /**
     * Extra headers to send with each request.
     */
    private String[] extraHeaders = null;
    /**
     * Extra query string to send with each request.
     */
    private String extraQuery = null;
    /**
     * Flag to indicate if the response contains extended information.
     * If false, a URL String is returned corresponding to the selected view value
     * If true, extended information is returned in a JSON format that can includes
     * a categorised breakdown of scores and priorities (if applicable).
     */
    private boolean extendedResponse = false;

    /**
     * Construct an SCIClient object with the specified licence key, and default
     * connection parameters.
     *
     * @param licenceKey The SCI licence key.
     */
    public SciClient(String licenceKey) {
        this.licenceKey = licenceKey;
    }

    /**
     * Construct an SCIClient object with the specified licence key, using the
     * specified host name to connect to the SCI server.
     *
     * @param licenceKey     The SCI licence key.
     * @param serverHostname The host name to use to connect.
     */
    public SciClient(String licenceKey, String serverHostname) {
        this.serverHostname = serverHostname;
        this.licenceKey = licenceKey;
    }

    /**
     * Construct an SCIClient object with the specified licence key, using the
     * specified host name and port to connect to the SCI server.
     *
     * @param licenceKey     The SCI licence key.
     * @param serverHostname The host name to use to connect.
     * @param serverPort     The TCP port number to use to connect.
     */
    public SciClient(String licenceKey, String serverHostname, int serverPort) {
        this.licenceKey = licenceKey;
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
    }

    /**
     * Construct an SCIClient object with the specified licence key, using the
     * specified host name and port to connect to the SCI server, and setting
     * whether or not to use SSL to connect.
     *
     * @param licenceKey     The SCI licence key.
     * @param serverHostname The host name to use to connect.
     * @param serverPort     The TCP port number to use to connect.
     * @param serverSecure   true to use SSL when connecting to the SCI server.
     */
    public SciClient(String licenceKey, String serverHostname, int serverPort, boolean serverSecure) {
        this.licenceKey = licenceKey;
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
        this.serverSecure = serverSecure;
    }

    /**
     * Get the host name used when connecting to the SCI server.
     *
     * @return The host name used to connect to the SCI server.
     */
    public String getServerHostname() {
        return (serverHostname == null) ? SCI_DEFAULT_SERVER : serverHostname;
    }

    /**
     * Set the host name of the SCI server.
     *
     * @param serverHostname The host name of the SCI server.
     */
    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    /**
     * Get the TCP port number used when connecting to the SCI server.
     *
     * @return The TCP port number to use.
     */
    public int getServerPort() {
        if (serverPort != 0) {
            return serverPort;
        }
        return serverSecure ? SCI_DEFAULT_SSL_PORT : SCI_DEFAULT_PORT;
    }

    /**
     * Set the TCP port number used when connecting to the SCI server.
     *
     * @param serverPort The TCP port number to use.
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Set whether or not the connection to the SCI server should use SSL.
     *
     * @param serverSecure true if the connection to the SSL server should use SSL.
     */
    public void setServerSecure(boolean serverSecure) {
        this.serverSecure = serverSecure;
    }

    /**
     * Check if the connection to the SCI server uses SSL.
     *
     * @return true if the connection uses SSL.
     */
    public boolean isServerSecure() {
        return serverSecure;
    }

    /**
     * Set whether or not HTTP POSTs are allowed to be proxied through us.
     *
     * @param postAllowed true if POSTs should be allowed.
     */
    public void setPostAllowed(boolean postAllowed) {
        this.postAllowed = postAllowed;
    }

    /**
     * Check if HTTP POSTs are allowed to be proxied through us.
     *
     * @return true if POSTs are allowed.
     */
    public boolean isPostAllowed() {
        return postAllowed;
    }

    /**
     * Set the host name of the HTTP proxy server to tunnel the SCI connection
     * through.
     *
     * @param sciProxyHostname the host name to use, or null to use no proxy.
     */
    public void setSciProxyHostname(String sciProxyHostname) {
        this.sciProxyHostname = sciProxyHostname;
    }

    /**
     * Get the host name of the HTTP proxy server we will tunnel SCI connections
     * through.
     *
     * @return the host name, or null if we are not using a proxy.
     */
    public String getSciProxyHostname() {
        return sciProxyHostname;
    }

    /**
     * Set the TCP port number of the HTTP proxy server to tunnel the SCI
     * connection through.
     *
     * @param sciProxyPort the sciProxyPort to set
     */
    public void setSciProxyPort(int sciProxyPort) {
        this.sciProxyPort = sciProxyPort;
    }

    /**
     * Get the TCP port number of the HTTP proxy server we will tunnel SCI
     * connections through.
     *
     * @return the port number
     */
    public int getSciProxyPort() {
        return sciProxyPort == 0 ? HTTP_PROXY_PORT : sciProxyPort;
    }

    /**
     * Sets extra HTTP headers to send with each request that is proxied through
     * this class. Each string in the array should be an RFC-822-style header,
     * with no newline or carriage return at the end (e.g.
     * "Cookie: UserAuth=12345678").
     *
     * @param extraHeaders the extra headers to send, or null to not send any
     */
    public void setExtraHeaders(String[] extraHeaders) {
        this.extraHeaders = extraHeaders;
    }

    /**
     * Gets the extra HTTP headers that have been requested to be sent with each
     * proxied request.
     *
     * @return the extra headers, or null if there are none
     */
    public String[] getExtraHeaders() {
        return extraHeaders;
    }

    /**
     * Sets an extra query string to be sent with each request that is proxied
     * through this class. The string should be simply the extra parameter(s) to
     * send, with no "?" or trailing or leading "&amp;" (e.g. "a=b" or "a=b&amp;c=d").
     *
     * @param extraQuery the extra query string to send, or null to send none
     */
    public void setExtraQuery(String extraQuery) {
        this.extraQuery = extraQuery;
    }

    /**
     * Gets the extra query string that will be sent with each request that is
     * proxied through this class.
     *
     * @return the extra query string which will be sent, or null
     */
    public String getExtraQuery() {
        return extraQuery;
    }

    /**
     * Sets a flag to indicate if the response contains extended information.
     * If false, a URL String is returned corresponding to the selected view value
     * If true, extended information is returned in a JSON format that can includes
     * a categorised breakdown of scores and priorities (if applicable).
     *
     * @param extendedResponse true or false
     */
    public void setExtendedResponse(boolean extendedResponse) {
        this.extendedResponse = extendedResponse;
    }

    /**
     * Get the flag that indicates if a simple or extended response will be returned.
     *
     * @return true or false.
     */
    public boolean getExtendedResponse() {
        return this.extendedResponse;
    }

    /**
     * Perform a Sitemorse test of the specified URL. Only the specific host
     * name identified in the URL itself will be proxied, all other host names
     * will be connected to directly by the Sitemorse test server. The
     * "snapshot-view" version of the results will be used.
     *
     * @param url The URL to test.
     * @return The URL of the finished report to display to the user.
     * @throws SciException if an error occurs
     */
    public String performTest(String url) throws SciException {
        return performTest(url, "snapshot-page");
    }

    /**
     * Perform a Sitemorse test of the specified URL. Only the specific host
     * name identified in the URL itself will be proxied, all other host names
     * will be connected to directly by the Sitemorse test server.
     *
     * @param url  The URL to test.
     * @param view The view that should be displayed to the user when the test is
     *             complete. This must be one of "report", "snapshot-page" or
     *             "snapshot-source".
     * @return The URL of the finished report to display to the user.
     * @throws SciException if an error occurs
     */
    public String performTest(String url, String view) throws SciException {
        String[] hostNames = new String[1];
        URL urlobj;

        try {
            urlobj = new URL(url);
        } catch (MalformedURLException e) {
            throw new SciException(e);
        }
        hostNames[0] = urlobj.getHost();
        return performTest(url, hostNames, view);
    }

    /**
     * Perform a Sitemorse test of the specified URL. All the specified host
     * names will be proxied via the SCI client. Note that the host name
     * indicated by the URL should be included in the host names list. The
     * "snapshot-view" version of the results will be used.
     *
     * @param url       The URL to test.
     * @param hostNames An array of host names to proxy.
     * @return The URL of the finished report to display to the user.
     * @throws SciException if an error occurs
     */
    public String performTest(String url, String[] hostNames) throws SciException {
        return performTest(url, hostNames, "snapshot-page");
    }

    /**
     * Perform a Sitemorse test of the specified URL. All the specified host
     * names will be proxied via the SCI client. Note that the host name
     * indicated by the URL should be included in the host names list.
     *
     * @param url       The URL to test.
     * @param hostNames An array of host names to proxy.
     * @param view      The view that should be displayed to the user when the test is
     *                  complete. This must be one of "report", "snapshot-page" or
     *                  "snapshot-source".
     * @return The URL of the finished report to display to the user.
     * @throws SciException if an error occurs
     */
    public String performTest(String url, String[] hostNames, String view) throws SciException {
        Socket sock = null;
        BufferedReader sciIn;
        BufferedWriter sciOut;
        String line;

        if (!(view.equals("report") || view.equals("snapshot-page") || view.equals("snapshot-source"))) {
            throw new SciException("Invalid 'view' parameter");
        }
        try {
            if (sciProxyHostname != null) {
                sock = new Socket();
                sock.connect(new InetSocketAddress(getSciProxyHostname(), getSciProxyPort()), SCI_CONNECT_TIMEOUT);
                sciIn = new BufferedReader(new InputStreamReader(sock.getInputStream(), SCI_CHARSET));
                sciOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), SCI_CHARSET));
                sciOut.write("CONNECT " + getServerHostname() + ":" + getServerPort() + " HTTP/1.0" + CRLF + CRLF);
                sciOut.flush();
                line = sciIn.readLine();
                if (line == null) {
                    throw new SciException("HTTP proxy dropped connection " + "after request");
                }
                if (!line.startsWith("HTTP/1.") || line.length() < 12) {
                    throw new SciException("Unknown status line " + "from HTTP proxy: " + line);
                }
                if (!line.substring(8, 12).equals(" 200")) {
                    throw new SciException("HTTP proxy server returned " + "error: " + line);
                }
                while (true) {
                    line = sciIn.readLine();
                    if (line == null) {
                        throw new SciException("HTTP proxy dropped connection " + "during response headers");
                    }
                    if (line.length() == 0) {
                        break;
                    }
                }
                if (serverSecure) {
                    sock = ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(sock, getServerHostname(), getServerPort(), true);
                }
            } else {
                if (serverSecure) {
                    sock = SSLSocketFactory.getDefault().createSocket();
                } else {
                    sock = new Socket();
                }
                sock.connect(new InetSocketAddress(getServerHostname(), getServerPort()), SCI_CONNECT_TIMEOUT);
            }
            sock.setSoTimeout(SCI_READ_TIMEOUT);
            sciIn = new BufferedReader(new InputStreamReader(sock.getInputStream(), SCI_CHARSET));
            sciOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), SCI_CHARSET));
            line = sciIn.readLine();
            if (line == null) {
                throw new SciException("SCI server immediately disconnected");
            }
            if (line.length() < 16 || !line.substring(0, 4).equals("SCI:")) {
                throw new SciException("Bad greeting line from SCI server");
            }
            if (!line.substring(4, 5).equals("1")) {
                throw new SciException("SCI server is using incompatible protocol version");
            }
            sciOut.write(generateAuthResponse(line.substring(8)) + CRLF);
            sciOut.flush();
            line = sciIn.readLine();
            if (line == null) {
                throw new SciException("SCI server disconnected" + " after authentication sent");
            }
            if (!line.equals("OK")) {
                throw new SciServerError(line);
            }
            JSONObject jsonreq = new JSONObject();
            jsonreq.put("url", url);
            jsonreq.put("hostNames", hostNames);
            jsonreq.put("view", view);
            if (this.extendedResponse) {
                jsonreq.put("extendedResponse", true);
            }
            line = jsonreq.toString();
            sciOut.write(line.length() + CRLF + line);
            sciOut.flush();
            line = sciIn.readLine();
            if (line == null) {
                throw new SciException("SCI server disconnected" + " after request data sent");
            }
            if (!line.equals("OK")) {
                throw new SciServerError(line);
            }
            return proxyRequests(sciIn, sciOut, hostNames);
        } catch (UnknownHostException e) {
            throw new SciException(e);
        } catch (IOException e) {
            throw new SciException(e);
        } catch (JSONException e) {
            throw new SciException(e);
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Generates the SCI authentication response string, using the configured
     * licenceKey and the challenge from the SCI server.
     *
     * @param challenge Challenge string from server.
     * @return Authentication response string.
     * @throws SciException if an error occurs
     */
    private String generateAuthResponse(String challenge) throws SciException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(licenceKey.substring(8).getBytes(SCI_CHARSET), SCI_HASH_ALGORITHM);
            Mac mac = Mac.getInstance(SCI_HASH_ALGORITHM);
            mac.init(signingKey);
            return licenceKey.substring(0, 8) + hexify(mac.doFinal(challenge.getBytes(SCI_CHARSET)));
        } catch (UnsupportedEncodingException e) {
            throw new SciException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new SciException(e);
        } catch (InvalidKeyException e) {
            throw new SciException(e);
        }
    }

    /**
     * Convert an array of bytes into a lower-case hexadecimal string
     * <p>
     * representation.
     *
     * @param bytes Byte array to convert.
     * @return String containing hexadecimal digits.
     */
    private String hexify(byte[] bytes) {
        StringBuilder s = new StringBuilder(bytes.length * 2);
        final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        for (int i = 0; i < bytes.length; i++) {
            s.append(hexChars[(bytes[i] >> 4) & 0x0f]);
            s.append(hexChars[bytes[i] & 0x0f]);
        }
        return s.toString();
    }

    /**
     * Implements the proxy phase of the SCI protocol. Repeatedly reads requests
     * from the SCI server, validates and fulfils them, and returns the results
     * to the server. Finishes when the server says the report is complete.
     *
     * @param sciIn     The reader for the SCI server socket.
     * @param sciOut    The writer for the SCI server socket.
     * @param hostNames The collection of host names we will proxy for.
     * @return The URL of the finished report to display to the user.
     * @throws SciException if an error occurs
     */
    private String proxyRequests(BufferedReader sciIn, BufferedWriter sciOut, String[] hostNames) throws SciException {
        String line;
        URL url;
        String method;
        String httpVersion;
        ArrayList<String> headers;
        Socket sock = null;
        int i;
        long timetarget;
        long now;
        long starttime;
        long resptime;
        long endtime;
        BufferedReader webIn;
        BufferedWriter webOut;
        String status;
        String path;
        String body;
        StringBuilder data;
        char[] buf = new char[BUFFER_SIZE];

        try {
            while (true) {
                /* Read a request line and see what it says */
                line = sciIn.readLine();
                if (line == null) {
                    throw new SciException("SCI server disconnected" + " during proxy phase");
                }
                if (line.equals("XSCI-NOOP")) {
                    /* no-op (timeout prevention), do nothing */
                    continue;
                } else if (line.startsWith("XSCI-COMPLETE ")) {
                    /* Test is complete, return the simple or extended response */
                    if (!this.extendedResponse) {
                        return line.substring(14);
                    }
                    headers = readHeaders(null, sciIn, 0);
                    body = readBody(headers, sciIn);
                    if (body == null) {
                        throw new SciException("Extended response headers missing Content-Length");
                    }
                    return body;
                } else if (line.startsWith("XSCI-ERROR ")) {
                    /* Fatal error, throw the parameter as an exception */
                    throw new SciServerError(line.substring(11));
                } else if (!line.startsWith("GET ") && !line.startsWith("POST ")) {
                    throw new SciException("Unknown SCI request: " + line);
                }
                /*
                 * It's an HTTP request, parse the request line and the URL, and
                 * read the headers.
                 */
                i = line.indexOf(" ");
                method = line.substring(0, i);
                i = line.lastIndexOf(" ");
                httpVersion = line.substring(i + 1);
                if (!httpVersion.startsWith("HTTP/1.") || httpVersion.length() != 8) {
                    throw new SciException("Unknown HTTP version: " + httpVersion);
                }
                url = new URL(line.substring(method.length() + 1, i));
                headers = readHeaders(null, sciIn, 0);
                body = readBody(headers, sciIn);

                /* Security checks on the request */
                if (method.equals("POST") && !postAllowed) {
                    sciOut.write("XSCI accessdenied " + "POST actions have been disallowed" + CRLF);
                    sciOut.flush();
                    continue;
                }
                if (!url.getProtocol().equals("http") && !url.getProtocol().equals("https")) {
                    sciOut.write("XSCI badscheme " + "URL scheme '" + url.getProtocol() + "' not allowed" + CRLF);
                    sciOut.flush();
                    continue;
                }
                for (i = 0; i < hostNames.length; i++) {
                    if (hostNames[i].equalsIgnoreCase(url.getHost())) {
                        break;
                    }
                }
                if (i >= hostNames.length) {
                    sciOut.write("XSCI accessdenied " + "CMS proxy access denied to host '" + url.getHost() + "'" + CRLF);
                    sciOut.flush();
                    continue;
                }
                if (url.getPort() != -1 && (url.getPort() < 1 || url.getPort() > 65535 || url.getPort() == 19 || url.getPort() == 25)) {
                    sciOut.write("XSCI badport " + "Access denied to port " + url.getPort() + CRLF);
                    sciOut.flush();
                    continue;
                }

                /*
                 * Forward the request to the relevant web server. This entire
                 * section must not take longer than WEB_TIMEOUT ms.
                 */
                try {
                    if (url.getProtocol().equals("https")) {
                        sock = SSLSocketFactory.getDefault().createSocket();
                    } else {
                        sock = new Socket();
                    }
                    timetarget = System.currentTimeMillis() + WEB_TIMEOUT;
                    /* Connect to the server */
                    starttime = System.currentTimeMillis();
                    sock.connect(new InetSocketAddress(url.getHost(), url.getPort() == -1 ? url.getDefaultPort() : url.getPort()), WEB_TIMEOUT);
                    webIn = new BufferedReader(new InputStreamReader(sock.getInputStream(), SCI_CHARSET));
                    webOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), SCI_CHARSET));

                    /*
                     * Calculate the path to send in the request.
                     * We may need to add parameters if this.extraQuery is set.
                     */
                    path = url.getFile();
                    if (extraQuery != null && extraQuery.length() > 0) {
                        if (url.getQuery() == null) {
                            path += "?" + extraQuery;
                        } else if (url.getQuery().length() == 0) {
                            path += extraQuery;
                        } else {
                            path += "&" + extraQuery;
                        }
                    }

                    /*
                     * Write the request line, the headers, and the body (if
                     * any)
                     */
                    webOut.write(method + " " + path + " " + httpVersion + CRLF);
                    for (i = 0; i < headers.size(); i++) {
                        webOut.write(headers.get(i) + CRLF);
                    }
                    if (extraHeaders != null) {
                        for (i = 0; i < extraHeaders.length; i++) {
                            webOut.write(extraHeaders[i] + CRLF);
                        }
                    }
                    webOut.write(CRLF);
                    if (body != null) {
                        webOut.write(body);
                    }
                    webOut.flush();

                    /*
                     * Wait for the status response line, then read the headers
                     */
                    now = System.currentTimeMillis();
                    if (now >= timetarget) {
                        throw new SocketTimeoutException();
                    }
                    sock.setSoTimeout((int) (timetarget - now));
                    status = webIn.readLine();
                    resptime = System.currentTimeMillis();
                    if (status == null) {
                        sciOut.write("XSCI noeoh " + "No end-of-headers found" + CRLF);
                        continue;
                    }
                    if (!status.startsWith("HTTP")) {
                        sciOut.write("XSCI badstatus Bad status line '" + line + "'" + CRLF);
                        continue;
                    }
                    now = System.currentTimeMillis();
                    if (now >= timetarget) {
                        throw new SocketTimeoutException();
                    }
                    headers = readHeaders(sock, webIn, (int) (timetarget - now));
                    if (headers == null) {
                        sciOut.write("XSCI noeoh " + "No end-of-headers found" + CRLF);
                        continue;
                    }
                    /*
                     * Read the response body, by reading all the data until the
                     * socket is closed by the other end.
                     */
                    data = new StringBuilder();
                    while (true) {
                        now = System.currentTimeMillis();
                        if (now >= timetarget) {
                            throw new SocketTimeoutException();
                        }
                        sock.setSoTimeout((int) (timetarget - now));
                        i = webIn.read(buf);
                        if (i == -1) {
                            break;
                        }
                        data.append(buf, 0, i);
                    }
                    endtime = System.currentTimeMillis();
                } catch (SocketTimeoutException e) {
                    sciOut.write("XSCI timeout Timeout reading from web server" + CRLF);
                    continue;
                } catch (ConnectException e) {
                    sciOut.write("XSCI connref Connection refused" + CRLF);
                    continue;
                } catch (UnknownHostException e) {
                    sciOut.write("XSCI noaddr Unknown hostname" + CRLF);
                    continue;
                } catch (IOException e) {
                    sciOut.write("XSCI exception " + e.toString() + CRLF);
                    continue;
                } finally {
                    if (sock != null) {
                        try {
                            sock.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    sock = null;
                    sciOut.flush();
                }
                /*
                 * Forward the response headers and body back to the SCI server.
                 * Remove any Content-Length header that may already exist, and
                 * add a new one that we can guarantee to be correct.
                 */
                sciOut.write(status + CRLF);
                for (i = 0; i < headers.size(); i++) {
                    if (!headers.get(i).toLowerCase().startsWith("content-length:")) {
                        sciOut.write(headers.get(i) + CRLF);
                    }
                }
                sciOut.write("Content-Length: " + data.length() + CRLF);
                sciOut.write("X-SCI-Response: " + (resptime - starttime) + CRLF);
                sciOut.write("X-SCI-TotalTime: " + (endtime - starttime) + CRLF);
                sciOut.write(CRLF);
                sciOut.write(data.toString());
                sciOut.flush();
            }
        } catch (IOException e) {
            throw new SciException(e);
        }
    }

    /**
     * Reads a set of RFC 822-style headers from a stream. If the socket and a
     * time-out are also provided, it will ensure that the entire operation
     * takes place within that time-out - otherwise, socket may be null and the
     * time-out should be 0. Returns null if the socket closes before the
     * end-of-headers marker is reached.
     *
     * @param sock    The underlying socket being read from.
     * @param in      The stream to read the data from.
     * @param timeout The time-out in milliseconds, or zero for no time-out.
     * @return An array of header strings.
     * @throws IOException if an error occurs
     */
    private ArrayList<String> readHeaders(Socket sock, BufferedReader in, int timeout) throws IOException {
        ArrayList<String> headers = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        String line;
        long timetarget;
        long now;

        timetarget = System.currentTimeMillis() + timeout;
        while (true) {
            if (timeout > 0) {
                now = System.currentTimeMillis();
                if (now >= timetarget) {
                    throw new SocketTimeoutException("Timeout reading headers");
                }
                sock.setSoTimeout((int) (timetarget - now));
            }
            line = in.readLine();
            if (line == null) {
                return null;
            }
            if (line.length() == 0) {
                if (buf.length() > 0) {
                    headers.add(buf.toString());
                }
                return headers;
            }
            if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
                buf.append(line);
            } else {
                if (buf.length() > 0) {
                    headers.add(buf.toString());
                    buf.setLength(0);
                }
                buf.append(line);
            }
        }
    }

    /**
     * Reads a message body from a stream. If the given headers contain
     * a Content-Length header, read that many bytes and return them as
     * a string. If there is no Content-Length, return null.
     *
     * @param headers The array of RFC822-style headers.
     * @param in      The stream to read the data from.
     * @return The body string, or null if there was no Content-Length.
     * @throws IOException  if an input/output error occurs
     * @throws SciException if the connection closes prematurely
     */
    private String readBody(ArrayList<String> headers, BufferedReader in) throws IOException, SciException {
        StringBuilder data;
        int i;
        int clen;
        char[] buf = new char[BUFFER_SIZE];

        for (i = 0; i < headers.size(); i++) {
            if (headers.get(i).toLowerCase().startsWith("content-length:")) {
                clen = Integer.parseInt(headers.get(i).substring(16));
                data = new StringBuilder(clen);
                while (clen > 0) {
                    i = in.read(buf, 0, clen > BUFFER_SIZE ? BUFFER_SIZE : clen);
                    if (i == -1) {
                        throw new SciException("SCI server " + " disconnected while sending" + " HTTP body");
                    }
                    data.append(buf, 0, i);
                    clen -= i;
                }
                return data.toString();
            }
        }
        return null;
    }

    /**
     * A test method which allows us to execute this class as an application to
     * see it in action.
     *
     * @param args No arguments are required.
     */
    public static void main(String[] args) {
        System.out.println("Testing SCI");
        System.out.println("Creating SCIClient using key: " + args[0]);
        SciClient client = new SciClient(args[0]);

        System.out.println("Testing page " + args[1]);
        System.out.println("Setting extended response");
        client.setExtendedResponse(true);
        try {
            System.out.println(client.performTest(args[1]));
        } catch (SciException e) {
            e.printStackTrace();
        }
    }
}
