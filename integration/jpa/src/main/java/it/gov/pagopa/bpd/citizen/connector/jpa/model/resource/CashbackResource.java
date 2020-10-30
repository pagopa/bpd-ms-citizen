package it.gov.pagopa.bpd.citizen.connector.jpa.model.resource;

import java.math.BigDecimal;

public class CashbackResource {

    private BigDecimal totalCashback;
    private Long transactionNumber;

    public BigDecimal getTotalCashback() {
        return totalCashback;
    }

    public void setTotalCashback(BigDecimal totalCashback) {
        this.totalCashback = totalCashback;
    }

    public Long getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(Long transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
}
