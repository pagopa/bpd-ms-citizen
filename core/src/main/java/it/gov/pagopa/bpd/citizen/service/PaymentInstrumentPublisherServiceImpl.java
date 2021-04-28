package it.gov.pagopa.bpd.citizen.service;

import eu.sia.meda.event.transformer.SimpleEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.PaymentInstrumentPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.PaymentInstrumentUpdate;
import org.springframework.stereotype.Service;

/**
 * Implementation of the PaymentInstrumentPublisherService, defines the service used for the interaction
 * with the PaymentInstrumentPublisherConnector
 */

@Service
public class PaymentInstrumentPublisherServiceImpl implements PaymentInstrumentPublisherService {

    private final PaymentInstrumentPublisherConnector paymentInstrumentPublisherConnector;
    private final SimpleEventRequestTransformer<PaymentInstrumentUpdate> simpleEventRequestTransformer;
    private final SimpleEventResponseTransformer simpleEventResponseTransformer;

    public PaymentInstrumentPublisherServiceImpl(PaymentInstrumentPublisherConnector paymentInstrumentPublisherConnector,
                                                 SimpleEventRequestTransformer<PaymentInstrumentUpdate> simpleEventRequestTransformer,
                                                 SimpleEventResponseTransformer simpleEventResponseTransformer) {
        this.paymentInstrumentPublisherConnector = paymentInstrumentPublisherConnector;
        this.simpleEventRequestTransformer = simpleEventRequestTransformer;
        this.simpleEventResponseTransformer = simpleEventResponseTransformer;
    }


    @Override
    public void publishPaymentInstrumentUpdateEvent(PaymentInstrumentUpdate paymentInstrumentUpdate) {
        paymentInstrumentPublisherConnector.doCall(
                paymentInstrumentUpdate, simpleEventRequestTransformer, simpleEventResponseTransformer);
    }
}
