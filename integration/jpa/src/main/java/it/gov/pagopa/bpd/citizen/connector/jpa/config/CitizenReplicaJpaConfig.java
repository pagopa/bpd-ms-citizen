package it.gov.pagopa.bpd.citizen.connector.jpa.config;

import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import it.gov.pagopa.bpd.common.connector.jpa.config.BaseJpaConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:config/CitizenReplicaJpaConnectionConfig.properties")
@EnableJpaRepositories(
        includeFilters = @ComponentScan.Filter(ReadOnlyRepository.class)
)
public class CitizenReplicaJpaConfig extends BaseJpaConfig {
}
