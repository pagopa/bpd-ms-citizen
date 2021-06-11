package it.gov.pagopa.bpd.citizen.connector.jpa.model.resource;

import java.math.BigDecimal;

public interface GetTotalCashbackResource {

    String getFiscalCode();

    Long getAwardPeriodId();

    BigDecimal getTotalCashback();

    BigDecimal getMaxTotalCashback();

    Long getTransactionNumber();

}
