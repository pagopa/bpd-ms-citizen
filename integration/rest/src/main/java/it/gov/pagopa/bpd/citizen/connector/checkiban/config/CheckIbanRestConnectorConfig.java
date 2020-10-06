package it.gov.pagopa.bpd.citizen.connector.checkiban.config;

import feign.Client;
import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestClient;
import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.security.KeyStore;

@Configuration
@Import(RestConnectorConfig.class)
@EnableFeignClients(clients = CheckIbanRestClient.class)
@PropertySource("classpath:config/BpdCitizenRestConnector.properties")
@Slf4j
public class CheckIbanRestConnectorConfig {

    @Value("${rest-client.checkiban.proxy.enabled}")
    private Boolean proxyEnabled;

    @Value("${rest-client.checkiban.proxy.host}")
    private Integer proxyHost;

    @Value("${rest-client.checkiban.proxy.host}")
    private String proxyPort;

    @Value("${rest-client.checkiban.proxy.username}")
    private String proxyUsername;

    @Value("${rest-client.checkiban.proxy.password}")
    private String proxyPassword;

    @Bean
    public Client getFeignClient() throws Exception {
        try {
            SSLSocketFactory sslSocketFactory = null;

//            if (mtlsEnabled) {
//                sslSocketFactory = getSSLSocketFactory();
//                log.debug("enabled socket factory: {}", sslSocketFactory);
//            }

            if (proxyEnabled) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                if (proxyUsername != null && !proxyUsername.equals("") &&
                        proxyPassword != null && !proxyPassword.equals("")) {
                    Client client = new Client.Proxied(sslSocketFactory,null, proxy,
                            proxyUsername, proxyPassword);
                    System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
                    Authenticator.setDefault(new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
                        }
                    });
                    return client;
                } else {
                    return new Client.Proxied(sslSocketFactory,null, proxy);
                }

            } else {
                return new Client.Default(sslSocketFactory, null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new Exception("Error occured while initializing feign client", e);
        }
    }
}
