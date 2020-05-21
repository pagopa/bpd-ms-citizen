package it.gov.pagopa.bpd.citizen.exception;

import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.common.exception.ResourceNotEnabledException;

public class CitizenNotEnabledException extends ResourceNotEnabledException {

    public CitizenNotEnabledException(String fiscalCode) {
        super(Citizen.class, fiscalCode);
    }

}
