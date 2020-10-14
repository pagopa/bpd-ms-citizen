package it.gov.pagopa.bpd.citizen.exception;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.common.exception.ResourceNotFoundException;

public class CitizenRankingNotFoundException extends ResourceNotFoundException {

    public CitizenRankingNotFoundException(String fiscalCode) {
        super(CitizenRanking.class, fiscalCode);
    }

}