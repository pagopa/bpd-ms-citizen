package it.gov.pagopa.bpd.citizen.service;

import eu.sia.meda.event.transformer.SimpleEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.PointTransactionPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;
import it.gov.pagopa.bpd.citizen.service.transformer.HeaderAwareRequestTransformer;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the PointTransactionPublisherService, defines the service used for the interaction
 * with the PointTransactionPublisherConnector
 */

@Service
public class CitizenStatusUpdatePublisherServiceImpl implements CitizenStatusUpdatePublisherService {

    private final CitizenStatusPublisherConnector citizenStatusPublisherConnectors;
    private final HeaderAwareRequestTransformer<StatusUpdate> simpleEventRequestTransformer;
    private final SimpleEventResponseTransformer simpleEventResponseTransformer;

    @Autowired
    public CitizenStatusUpdatePublisherServiceImpl(CitizenStatusPublisherConnector citizenStatusPublisherConnectors,
                                                   HeaderAwareRequestTransformer<StatusUpdate> simpleEventRequestTransformer,
                                                   SimpleEventResponseTransformer simpleEventResponseTransformer) {
        this.citizenStatusPublisherConnectors = citizenStatusPublisherConnectors;
        this.simpleEventRequestTransformer = simpleEventRequestTransformer;
        this.simpleEventResponseTransformer = simpleEventResponseTransformer;
    }

    /**
     * Calls the PointTransactionPublisherService, passing the transaction to be used as message payload
     *
     * @param statusUpdate Transaction instance to be used as payload for the outbound channel used bu the related connector
     */

    @Override
    public void publishCitizenStatus(StatusUpdate statusUpdate) {
        RecordHeaders recordHeaders = new RecordHeaders();
        recordHeaders.add("CITIZEN_STATUS_UPDATE", "true".getBytes());
        citizenStatusPublisherConnectors.doCall(
                statusUpdate, simpleEventRequestTransformer, simpleEventResponseTransformer, recordHeaders);
    }
}
