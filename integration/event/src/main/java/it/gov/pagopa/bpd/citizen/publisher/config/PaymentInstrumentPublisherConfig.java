package it.gov.pagopa.bpd.citizen.publisher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the PaymentInstrumentPublisherConfig
 */

@Configuration
@PropertySource("classpath:config/paymentInstrumentPublisher.properties")
public class PaymentInstrumentPublisherConfig {
}
