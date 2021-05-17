package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;

import java.time.OffsetDateTime;

/**
 * public interface for the PointTransactionPublisherService
 */

public interface PointTransactionPublisherService {

    /**
     * Method that has the logic for publishing a Transaction to the point-processor outbound channel,
     * calling on the appropriate connector
     *
     * @param outgoingTransaction OutgoingTransaction instance to be published
     */
    void publishPointTransactionEvent(Transaction outgoingTransaction, OffsetDateTime validationDateTime);
}
