package it.gov.pagopa.bpd.citizen.publisher;

import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.transformer.IEventRequestTransformer;
import eu.sia.meda.event.transformer.IEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import org.springframework.stereotype.Service;

@Service
public class CitizenStatusPublisherConnector extends BaseEventConnector<StatusUpdate, Boolean, StatusUpdate, Void> {

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
            Object... args) throws Exception {
        throw new Exception("test");
        //return this.call(statusUpdate, requestTransformer, responseTransformer, args);
    }
}