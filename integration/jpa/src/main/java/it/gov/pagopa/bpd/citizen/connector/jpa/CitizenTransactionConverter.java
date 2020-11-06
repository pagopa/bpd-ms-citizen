package it.gov.pagopa.bpd.citizen.connector.jpa;

public interface CitizenTransactionConverter {

    Long getRanking();

    Long getTotalParticipants();

    Long getMaxTrxNumber();

    Long getMinTrxNumber();

    Long getTrxNumber();

    Long getAwardPeriodId();
}
