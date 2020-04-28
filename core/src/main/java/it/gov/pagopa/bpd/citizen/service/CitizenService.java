package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.citizen.dao.model.CitizenRanking;


/**
 * A service to manage the Business Logic related to Citizen
 */
public interface CitizenService {

    Citizen find(String fiscalCode);

    Citizen update(String fiscalCode, Citizen cz);

    Citizen patch(String fiscalCode, Citizen cz);

    void delete(String fiscalCode);

    Long calculateAttendeesNumber();

    CitizenRanking findRanking(String fiscalCode, Long awardPeriodId);

}
