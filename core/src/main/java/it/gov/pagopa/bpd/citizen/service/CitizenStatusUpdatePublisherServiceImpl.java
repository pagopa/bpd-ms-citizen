package it.gov.pagopa.bpd.citizen.service;

import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.service.transformer.HeaderAwareRequestTransformer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * Implementation of the PointTransactionPublisherService, defines the service used for the interaction
 * with the PointTransactionPublisherConnector
 */

@Service
@Slf4j
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

    @SneakyThrows
    @Override
    @Retryable(
            maxAttemptsExpression= "${core.citizen.publish.retry.maxAttempts}",
            value=RuntimeException.class,
            backoff = @Backoff(
                    delayExpression = "${core.citizen.publish.retry.delay}",
                    multiplierExpression = "${core.citizen.publish.retry.multiplierExpression}",
                    maxDelayExpression = "${core.citizen.publish.retry.maxDelayExpression}"
            )
    )
    public void publishCitizenStatus(StatusUpdate statusUpdate) {
        RecordHeaders recordHeaders = new RecordHeaders();
        recordHeaders.add("CITIZEN_STATUS_UPDATE", "ALL".getBytes());
        citizenStatusPublisherConnectors.doCall(
                statusUpdate, simpleEventRequestTransformer, simpleEventResponseTransformer, recordHeaders);
    }

}
