package it.gov.pagopa.bpd.citizen.publisher.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

/**
 * Configuration class for the CitizenStatusPublisherConfig
 */

@Configuration
@EnableKafka
@ConditionalOnClass({KafkaTemplate.class})
@EnableConfigurationProperties({KafkaProperties.class})
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
@PropertySource("classpath:config/citizenStatusPublisher.properties")
public class CitizenStatusPublisherConfig {


    @Bean("citizenKafkaProperties")
    @ConfigurationProperties("spring.citizen.kafka")
    public KafkaProperties citizenKafkaProperties() {
        return new KafkaProperties();
    }


    @Bean("citizenKafkaTemplate")
    @ConditionalOnMissingBean(KafkaTemplate.class)
    public KafkaTemplate<?, ?> kafkaTemplate(
            @Qualifier("citizenKafkaProperties") KafkaProperties kafkaProperties,
            @Qualifier("citizenKafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(
                kafkaProducerFactory);
        kafkaTemplate.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @Bean("citizenKafkaProducerFactory")
    @ConditionalOnMissingBean(ProducerFactory.class)
    public ProducerFactory<?, ?> kafkaProducerFactory(
            @Qualifier("citizenKafkaProperties") KafkaProperties kafkaProperties) {
        DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties());
        String transactionIdPrefix = kafkaProperties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    @Bean("citizenKafkaTransactionManager")
    @ConditionalOnProperty(name = "spring.citizen.kafka.producer.transaction-id-prefix")
    @ConditionalOnMissingBean
    public KafkaTransactionManager<?, ?> kafkaTransactionManager(
            @Qualifier("citizenKafkaProducerFactory") ProducerFactory<?, ?> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }



}
