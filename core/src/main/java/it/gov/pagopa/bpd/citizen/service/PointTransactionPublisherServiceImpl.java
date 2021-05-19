package it.gov.pagopa.bpd.citizen.service;

import eu.sia.meda.event.transformer.SimpleEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.PointTransactionPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * Implementation of the PointTransactionPublisherService, defines the service used for the interaction
 * with the PointTransactionPublisherConnector
 */

@Service
public class PointTransactionPublisherServiceImpl implements PointTransactionPublisherService {

    private final PointTransactionPublisherConnector pointTransactionPublisherConnector;
    private final SimpleEventRequestTransformer<Transaction> simpleEventRequestTransformer;
    private final SimpleEventResponseTransformer simpleEventResponseTransformer;

    @Autowired
    public PointTransactionPublisherServiceImpl(ObjectProvider<PointTransactionPublisherConnector> pointTransactionPublisherConnector,
                                                SimpleEventRequestTransformer<Transaction> simpleEventRequestTransformer,
                                                SimpleEventResponseTransformer simpleEventResponseTransformer) {
        this.pointTransactionPublisherConnector = pointTransactionPublisherConnector.getIfAvailable();
        this.simpleEventRequestTransformer = simpleEventRequestTransformer;
        this.simpleEventResponseTransformer = simpleEventResponseTransformer;
    }

    /**
     * Calls the PointTransactionPublisherService, passing the transaction to be used as message payload
     *
     * @param transaction Transaction instance to be used as payload for the outbound channel used bu the related connector
     */

    @Override
    public void publishPointTransactionEvent(Transaction transaction, OffsetDateTime validationDateTime) {
        RecordHeaders recordHeaders = new RecordHeaders();
        recordHeaders.add("CITIZEN_VALIDATION_DATETIME", validationDateTime.toString().getBytes());
        pointTransactionPublisherConnector.doCall(
                transaction, simpleEventRequestTransformer, simpleEventResponseTransformer, recordHeaders);
    }
}
