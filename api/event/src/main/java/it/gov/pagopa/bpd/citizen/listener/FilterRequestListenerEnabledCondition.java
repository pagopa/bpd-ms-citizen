package it.gov.pagopa.bpd.citizen.listener;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class FilterRequestListenerEnabledCondition extends AllNestedConditions {

    public FilterRequestListenerEnabledCondition() {
        super(ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "listeners.eventConfigurations.items.OnTransactionFilterRequestListener",
            name = "enable",
            havingValue = "true")
    public static class FilterRequestListenerEnabled {

    }

}
