package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.publisher.model.PaymentInstrumentUpdate;

public interface PaymentInstrumentPublisherService {

    /**
     * Method that has the logic for publishing a PaymentInstrumentUpdate to the payment-instrument outbound channel,
     * calling on the appropriate connector
     *
     * @param paymentInstrumentUpdate PaymentInsrumentUpdate instance to be published
     */
    void publishPaymentInstrumentUpdateEvent(PaymentInstrumentUpdate paymentInstrumentUpdate);
}
