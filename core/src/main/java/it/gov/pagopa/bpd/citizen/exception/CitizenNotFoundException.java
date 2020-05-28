package it.gov.pagopa.bpd.citizen.exception;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.common.exception.ResourceNotFoundException;

public class CitizenNotFoundException extends ResourceNotFoundException {

    public CitizenNotFoundException(String fiscalCode) {
        super(Citizen.class, fiscalCode);
    }

}