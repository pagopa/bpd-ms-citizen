package it.gov.pagopa.bpd.citizen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@PropertySource("classpath:config/coreConfig.properties")
public class RetryConfig {}
