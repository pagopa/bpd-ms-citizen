package it.gov.pagopa.bpd.citizen.connector.checkiban.config;

import feign.Client;
import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestClient;
import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import javax.net.ssl.SSLSocketFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
@Import(RestConnectorConfig.class)
@EnableFeignClients(clients = CheckIbanRestClient.class)
@PropertySource("classpath:config/BpdCitizenRestConnector.properties")
@Slf4j
public class CheckIbanRestConnectorConfig {
//
//    @Value("${rest-client.checkiban.proxy.enabled}")
//    private Boolean proxyEnabled;
//
//    @Value("${rest-client.checkiban.proxy.host}")
//    private String proxyHost;
//
//    @Value("${rest-client.checkiban.proxy.port}")
//    private Integer proxyPort;
//
//    @Bean
//    public Client getFeignClient() throws Exception {
//        try {
//            SSLSocketFactory sslSocketFactory = null;
//
//            if (proxyEnabled) {
//                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
//                    return new Client.Proxied(sslSocketFactory,null, proxy);
//            } else {
//                return new Client.Default(sslSocketFactory, null);
//            }
//
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//            throw new Exception("Error occured while initializing feign client", e);
//        }
//
//
//    }
}
