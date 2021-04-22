package it.gov.pagopa.bpd.citizen.connector.jpa;

import java.math.BigDecimal;

public interface CitizenTransactionMilestoneConverter extends CitizenTransactionConverter {

    String getIdTrxPivot();

    BigDecimal getCashbackNorm();

    String getIdTrxMinTransactionNumber();

    BigDecimal getTotalCashback();

    BigDecimal getMaxCashback();
}
