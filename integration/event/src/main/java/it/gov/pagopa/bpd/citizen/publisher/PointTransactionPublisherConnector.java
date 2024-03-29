package it.gov.pagopa.bpd.citizen.publisher;

import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.transformer.IEventRequestTransformer;
import eu.sia.meda.event.transformer.IEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * Class extending the MEDA BaseEventConnector, is responsible for calling a Kafka outbound channel with messages
 * containing a json mapped on the Transaction class
 */

@Service
@Conditional(TransactionPublisherEnabledCondition.class)
public class PointTransactionPublisherConnector extends BaseEventConnector<Transaction, Boolean, Transaction, Void> {

    /**
     * @param transaction         Transaction instance to be used as message content
     * @param requestTransformer  Trannsformer for the request data
     * @param responseTransformer Transformer for the call response
     * @param args                Additional args to be used in the call
     * @return Exit status for the call
     */
    public Boolean doCall(
            Transaction transaction, IEventRequestTransformer<Transaction,
            Transaction> requestTransformer,
            IEventResponseTransformer<Void, Boolean> responseTransformer,
            Object... args) {
        return this.call(transaction, requestTransformer, responseTransformer, args);
    }
}
