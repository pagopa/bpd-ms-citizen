package it.gov.pagopa.bpd.citizen.publisher;

import eu.sia.meda.core.interceptors.BaseContextHolder;
import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.request.EventRequest;
import eu.sia.meda.event.response.EventResponse;
import eu.sia.meda.event.transformer.IEventRequestTransformer;
import eu.sia.meda.event.transformer.IEventResponseTransformer;
import eu.sia.meda.layers.connector.BaseConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@Service
public class CitizenStatusPublisherConnector extends BaseConnector<EventRequest<StatusUpdate>, EventResponse<Void>> {

    @Autowired
    @Qualifier("citizenKafkaTemplate")
    private KafkaTemplate kafkaTemplate;

    /**
     * @param statusUpdate        StatusUpdate instance to be used as message content
     * @param requestTransformer  Transformer for the request data
     * @param responseTransformer Transformer for the call response
     * @param args                Additional args to be used in the call
     * @return Exit status for the call
     */
    public Boolean doCall(
            StatusUpdate statusUpdate, IEventRequestTransformer<StatusUpdate, StatusUpdate> requestTransformer,
            IEventResponseTransformer<Void, Boolean> responseTransformer,
            Object... args) {
        EventRequest<StatusUpdate> eventRequest = requestTransformer.transform(statusUpdate, args);
        this.doPreExecute(eventRequest);
        EventResponse eventResponse;

        if (eventRequest.getPayload() == null) {
            eventResponse = new EventResponse(false, "Payload cannot be null");
        } else {
            Headers headers = eventRequest.getHeaders();
            if (headers == null) {
                headers = new RecordHeaders();
                eventRequest.setHeaders((Headers) headers);
            }

            if (headers.lastHeader("x-request-id") == null) {
                headers.add("x-request-id", BaseContextHolder.getApplicationContext()
                        .getRequestId().getBytes(StandardCharsets.UTF_8));
            }

            if (headers.lastHeader("x-originapp") == null) {
                headers.add("x-originapp", BaseContextHolder.getApplicationContext()
                        .getOriginApp().getBytes(StandardCharsets.UTF_8));
            }

            if (headers.lastHeader("x-user-id") == null) {
                headers.add("x-user-id", BaseContextHolder.getApplicationContext()
                        .getUserId().getBytes(StandardCharsets.UTF_8));
            }

            String topicTmp;
            if (eventRequest.getTopic() != null) {
                topicTmp = eventRequest.getTopic();
            } else {
                topicTmp = "bpd-trx-cashback";
            }

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(String.format(
                        "Sending message on topic '%s' towards bootstrap servers '%s'", topicTmp, ""));
            }

            eventResponse = new EventResponse(true, "Success", this.kafkaTemplate.send(
                    new ProducerRecord("bpd-trx-cashback", null, null, eventRequest.getKey(),
                            eventRequest.getPayload(), headers)));
        }

        return responseTransformer.transform(eventResponse);
    }

}