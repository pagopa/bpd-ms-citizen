package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenTransaction;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.CashbackResource;


/**
 * A service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    Citizen find(String fiscalCode);

    Citizen update(String fiscalCode, Citizen cz);

    String patch(String fiscalCode, Citizen cz);

    void delete(String fiscalCode);

    Long calculateAttendeesNumber();

    CitizenTransaction getTransactionDetails(String fiscalCode, Long awardPeriodId);

    CitizenRanking findRanking(String hpan, String fiscalCode, Long awardPeriodId);

    CashbackResource getCashback(String hpan, String fiscalCode, Long awardPeriodId);

}
