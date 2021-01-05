package it.gov.pagopa.bpd.citizen.connector.jpa.config;

import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import it.gov.pagopa.bpd.common.connector.jpa.config.BaseJpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@PropertySource("classpath:config/CitizenJpaConnectionConfig.properties")
@EnableJpaRepositories(
        excludeFilters = @ComponentScan.Filter(ReadOnlyRepository.class)
)
public class CitizenJpaConfig extends BaseJpaConfig {
}
