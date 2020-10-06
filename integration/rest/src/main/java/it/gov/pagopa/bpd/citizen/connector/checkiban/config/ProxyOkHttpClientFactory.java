package it.gov.pagopa.bpd.citizen.connector.checkiban.config;

import okhttp3.OkHttpClient;
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class ProxyOkHttpClientFactory extends DefaultOkHttpClientFactory {
    public ProxyOkHttpClientFactory(OkHttpClient.Builder builder) {
        super(builder);
    }

    @Override
    public OkHttpClient.Builder createBuilder(boolean disableSslValidation) {
        OkHttpClient.Builder builder = super.createBuilder(true);
        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.70.66.101", 8181)));
        return builder;
    }
}