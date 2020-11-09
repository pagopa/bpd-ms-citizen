package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;

import java.util.List;


/**
 * A service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    Citizen find(String fiscalCode);

    Citizen update(String fiscalCode, Citizen cz);

    String patch(String fiscalCode, Citizen cz);

    void delete(String fiscalCode);

    CitizenRanking getTotalCashback(CitizenRankingId id);

    List<CitizenTransactionConverter> findRankingDetails(String fiscalCode, Long awardPeriodId);

}
