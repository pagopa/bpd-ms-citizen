package it.gov.pagopa.bpd.citizen.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class CitizenInvalidIbanException extends MedaDomainRuntimeException {

    public <K extends Serializable> CitizenInvalidIbanException(String iban) {
        super("INVALID IBAN", "iban.invalid.sepa.error", HttpStatus.BAD_REQUEST);
    }


}
