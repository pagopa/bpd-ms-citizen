package it.gov.pagopa.bpd.citizen.connector.jpa;

public interface CitizenTransactionConverter {

    String getFiscalCode();

    Long getRanking();

    Long getTotalParticipants();

    Long getMaxTrxNumber();

    Long getMinTrxNumber();

    Long getTrxNumber();

    Long getAwardPeriodId();
}
