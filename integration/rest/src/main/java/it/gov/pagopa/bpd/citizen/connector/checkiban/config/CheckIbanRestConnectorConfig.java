package it.gov.pagopa.bpd.citizen.connector.checkiban.config;

import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestClient;
import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestConnectorConfig.class)
@EnableFeignClients(clients = CheckIbanRestClient.class)
@PropertySource("classpath:config/BpdCitizenRestConnector.properties")
public class CheckIbanRestConnectorConfig {
}
