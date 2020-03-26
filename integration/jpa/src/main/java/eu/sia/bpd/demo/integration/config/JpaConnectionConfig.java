package eu.sia.bpd.demo.integration.config;

import eu.sia.meda.connector.jpa.config.JPAConnectorConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@ConditionalOnMissingBean(name = "JPADataSource")
@Configuration
@PropertySource("classpath:config/jpaConnectionConfig.properties")
public class JpaConnectionConfig extends JPAConnectorConfig {
}
