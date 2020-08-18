package it.gov.pagopa.bpd.citizen.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;

public class CitizenNotFoundException extends MedaDomainRuntimeException {

    public CitizenNotFoundException(String fiscalCode) {
        super(getMessage(Citizen.class, fiscalCode), "user.not-found.error", HttpStatus.NOT_FOUND);
    }

    private static String getMessage(Class<?> resourceClass, Object id) {
        return String.format("USER NOT FOUND", resourceClass.getSimpleName(), id);
    }

}