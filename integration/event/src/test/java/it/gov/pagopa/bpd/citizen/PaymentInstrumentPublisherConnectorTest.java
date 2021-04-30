package it.gov.pagopa.bpd.citizen;

import eu.sia.meda.event.BaseEventConnectorTest;
import eu.sia.meda.util.TestUtils;
import it.gov.pagopa.bpd.citizen.publisher.PaymentInstrumentPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.PaymentInstrumentUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

/**
 * Test class for the PointTransactionPublisherConnector class
 */

@Import({PaymentInstrumentPublisherConnector.class})
@TestPropertySource(
        locations = "classpath:config/testPaymentInstrumentPublisher.properties",
        properties = {
                "connectors.eventConfigurations.items.PaymentInstrumentPublisherConnector.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class PaymentInstrumentPublisherConnectorTest extends
        BaseEventConnectorTest<PaymentInstrumentUpdate, Boolean, PaymentInstrumentUpdate, Void, PaymentInstrumentPublisherConnector> {

    @Value("${connectors.eventConfigurations.items.PaymentInstrumentPublisherConnector.topic}")
    private String topic;

    @Autowired
    private PaymentInstrumentPublisherConnector paymentInstrumentPublisherConnector;

    @Override
    protected PaymentInstrumentPublisherConnector getEventConnector() {
        return paymentInstrumentPublisherConnector;
    }

    @Override
    protected PaymentInstrumentUpdate getRequestObject() {
        return TestUtils.mockInstance(new PaymentInstrumentUpdate());
    }

    @Override
    protected String getTopic() {
        return topic;
    }
}
