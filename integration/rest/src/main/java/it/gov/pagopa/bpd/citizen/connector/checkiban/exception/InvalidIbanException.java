package it.gov.pagopa.bpd.citizen.connector.checkiban.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

public class InvalidIbanException extends MedaDomainRuntimeException {


    public InvalidIbanException() {
        super("Invalid IBAN", "resource.invalid.error", HttpStatus.BAD_REQUEST);
    }

}