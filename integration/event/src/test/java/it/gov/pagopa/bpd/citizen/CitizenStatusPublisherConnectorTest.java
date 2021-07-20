package it.gov.pagopa.bpd.citizen;

import eu.sia.meda.event.BaseEventConnectorTest;
import eu.sia.meda.util.TestUtils;
import it.gov.pagopa.bpd.citizen.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.PointTransactionPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

/**
 * Test class for the PointTransactionPublisherConnector class
 */

@Import({CitizenStatusPublisherConnector.class})
@TestPropertySource(
        locations = "classpath:config/testCitizenStatusPublisher.properties",
        properties = {
                "connectors.eventConfigurations.items.CitizenStatusPublisherConnector.bootstrapServers=${spring.embedded.kafka.brokers}",
                "connectors.eventConfigurations.items.CitizenStatusPublisherConnector.enable=true"
        })
public class CitizenStatusPublisherConnectorTest {
//        extends BaseEventConnectorTest<StatusUpdate, Boolean, StatusUpdate, Void, CitizenStatusPublisherConnector> {
//
//    @Value("${connectors.eventConfigurations.items.CitizenStatusPublisherConnector.topic}")
//    private String topic;
//
//    @Autowired
//    private CitizenStatusPublisherConnector citizenStatusPublisherConnector;
//
//    @Override
//    protected CitizenStatusPublisherConnector getEventConnector() {
//        return citizenStatusPublisherConnector;
//    }
//
//    @Override
//    protected StatusUpdate getRequestObject() {
//        return TestUtils.mockInstance(new StatusUpdate());
//    }
//
//    @Override
//    protected String getTopic() {
//        return topic;
//    }
}
