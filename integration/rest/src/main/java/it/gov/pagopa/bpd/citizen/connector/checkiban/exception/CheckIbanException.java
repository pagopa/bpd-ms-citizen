package it.gov.pagopa.bpd.citizen.connector.checkiban.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import feign.Response;
import org.springframework.http.HttpStatus;

public class CheckIbanException extends MedaDomainRuntimeException {

    public CheckIbanException(Response response) {
        super(response.reason(), "generic.error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}