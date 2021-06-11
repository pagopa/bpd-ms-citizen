package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionMilestoneConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.GetTotalCashbackResource;

import java.util.List;


/**
 * A service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    Citizen find(String fiscalCode);

    Citizen update(String fiscalCode, Citizen cz);

    String patch(String fiscalCode, Citizen cz);

    Boolean delete(String fiscalCode);

    GetTotalCashbackResource getTotalCashback(CitizenRankingId id);

    List<CitizenTransactionConverter> findRankingDetails(String fiscalCode, Long awardPeriodId);

    List<CitizenTransactionMilestoneConverter> findRankingMilestoneDetails(String fiscalCode, Long awardPeriodId);

}
