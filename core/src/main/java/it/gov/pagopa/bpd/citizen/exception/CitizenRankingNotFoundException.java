package it.gov.pagopa.bpd.citizen.exception;

import it.gov.pagopa.bpd.citizen.dao.model.CitizenRanking;
import it.gov.pagopa.bpd.common.exception.ResourceNotFoundException;

public class CitizenRankingNotFoundException extends ResourceNotFoundException {

    public CitizenRankingNotFoundException(String fiscalCode) {
        super(CitizenRanking.class, fiscalCode);
    }

}