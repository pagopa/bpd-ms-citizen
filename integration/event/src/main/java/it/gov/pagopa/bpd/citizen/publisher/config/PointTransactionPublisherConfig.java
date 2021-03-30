package it.gov.pagopa.bpd.citizen.publisher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the PointTransactionPublisherConfig
 */

@Configuration
@PropertySource("classpath:config/pointTransactionPublisher.properties")
public class PointTransactionPublisherConfig {
}
