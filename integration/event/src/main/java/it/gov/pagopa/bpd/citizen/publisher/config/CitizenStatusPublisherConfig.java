package it.gov.pagopa.bpd.citizen.publisher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the CitizenStatusPublisherConfig
 */

@Configuration
@PropertySource("classpath:config/citizenStatusPublisher.properties")
public class CitizenStatusPublisherConfig {
}
