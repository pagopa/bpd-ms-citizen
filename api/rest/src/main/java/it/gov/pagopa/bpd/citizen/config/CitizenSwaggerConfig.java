package it.gov.pagopa.bpd.citizen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class CitizenSwaggerConfig {

    @Configuration
    @Profile("swaggerIT")
    @PropertySource("classpath:/swagger/swagger_it_IT.properties")

    public static class itConfig {

    }
}
