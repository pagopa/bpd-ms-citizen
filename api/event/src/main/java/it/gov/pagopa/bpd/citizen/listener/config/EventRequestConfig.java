package it.gov.pagopa.bpd.citizen.listener.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the OnTransactionSaveRequestListener class
 */

@Configuration
@PropertySource("classpath:config/transactionRequestListener.properties")
public class EventRequestConfig {
}
