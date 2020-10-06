package it.gov.pagopa.bpd.citizen.connector.checkiban.config;

import feign.Client;
import feign.Util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Proxied extends Client.Default {
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    private final Proxy proxy;
    private String credentials;

    public Proxied(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy) {
        super(sslContextFactory, hostnameVerifier);
        Util.checkNotNull(proxy, "a proxy is required.", new Object[0]);
        this.proxy = proxy;
    }

    public Proxied(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier, Proxy proxy, String proxyUser, String proxyPassword) {
        this(sslContextFactory, hostnameVerifier, proxy);
        Util.checkArgument(Util.isNotBlank(proxyUser), "proxy user is required.", new Object[0]);
        Util.checkArgument(Util.isNotBlank(proxyPassword), "proxy password is required.", new Object[0]);
        this.credentials = this.basic(proxyUser, proxyPassword);
    }

    public HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
        if (Util.isNotBlank(this.credentials)) {
            connection.addRequestProperty("Proxy-Authorization", this.credentials);
        }

        return connection;
    }

    public String getCredentials() {
        return this.credentials;
    }

    private String basic(String username, String password) {
        String token = username + ":" + password;
        byte[] bytes = token.getBytes(StandardCharsets.ISO_8859_1);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return "Basic " + encoded;
    }
}