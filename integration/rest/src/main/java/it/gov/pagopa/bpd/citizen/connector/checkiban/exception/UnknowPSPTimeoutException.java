package it.gov.pagopa.bpd.citizen.connector.checkiban.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

public class UnknowPSPTimeoutException extends MedaDomainRuntimeException {

    public UnknowPSPTimeoutException() {
        super("UNKNOW PSP TIMEOUT", "psp.unknow.timeout.error", HttpStatus.BAD_GATEWAY);
    }
}