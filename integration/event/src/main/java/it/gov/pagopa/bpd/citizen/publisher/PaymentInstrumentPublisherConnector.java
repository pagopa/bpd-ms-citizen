package it.gov.pagopa.bpd.citizen.publisher;

import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.transformer.IEventRequestTransformer;
import eu.sia.meda.event.transformer.IEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.model.PaymentInstrumentUpdate;
import org.springframework.stereotype.Service;

@Service
public class PaymentInstrumentPublisherConnector
        extends BaseEventConnector<PaymentInstrumentUpdate, Boolean, PaymentInstrumentUpdate, Void> {

    /**
     * @param paymentInstrumentUpdate PaymentInstrumentUpdate instance to be used as message content
     * @param requestTransformer      Trannsformer for the request data
     * @param responseTransformer     Transformer for the call response
     * @param args                    Additional args to be used in the call
     * @return Exit status for the call
     */
    public Boolean doCall(
            PaymentInstrumentUpdate paymentInstrumentUpdate, IEventRequestTransformer<PaymentInstrumentUpdate,
            PaymentInstrumentUpdate> requestTransformer,
            IEventResponseTransformer<Void, Boolean> responseTransformer,
            Object... args) {
        return this.call(paymentInstrumentUpdate, requestTransformer, responseTransformer, args);
    }
}
