package it.gov.pagopa.bpd.citizen.connector.jpa.model;


public class CitizenTransaction {

    Long ranking;
    Long totalParticipants;
    Long maxTrxNumber;
    Long minTrxNumber;
    Long trxNumber;
    Long awardPeriodId;


    public Long getRanking() {
        return ranking;
    }

    public Long getTotalParticipants() {
        return totalParticipants;
    }

    public Long getMaxTrxNumber() {
        return maxTrxNumber;
    }

    public Long getMinTrxNumber() {
        return minTrxNumber;
    }

    public Long getTrxNumber() {
        return trxNumber;
    }

    public Long getAwardPeriodId() {
        return awardPeriodId;
    }


}
