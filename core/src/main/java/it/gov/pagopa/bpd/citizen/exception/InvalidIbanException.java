package it.gov.pagopa.bpd.citizen.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class InvalidIbanException extends MedaDomainRuntimeException {

    private static final String CODE = "resource.invalid.error";
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;


    private static String getMessage(String resourceName, Object id) {
        return String.format("", resourceName, id);
    }

    public <K extends Serializable> InvalidIbanException(String id) {
        super(getMessage("IBAN", id), CODE, STATUS);
    }

}