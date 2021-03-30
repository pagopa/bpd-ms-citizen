package it.gov.pagopa.bpd.citizen.publisher;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class TransactionPublisherEnabledCondition extends AllNestedConditions {

    public TransactionPublisherEnabledCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "connectors.eventConfigurations.items.PointTransactionPublisherConnector",
            name = "enable",
            havingValue = "true")
    public static class FilterRequestListenerEnabled {

    }
}
