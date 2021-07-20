package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;

/**
 * public interface for the PointTransactionPublisherService
 */

public interface CitizenStatusUpdatePublisherService {

    /**
     * Method that has the logic for publishing a StatusUpdate to the winning-transaction outbound channel,
     * calling on the appropriate connector
     *
     * @param statusUpdate StatusUpdate instance to be published
     */
    void publishCitizenStatus(StatusUpdate statusUpdate) throws Exception;

}
