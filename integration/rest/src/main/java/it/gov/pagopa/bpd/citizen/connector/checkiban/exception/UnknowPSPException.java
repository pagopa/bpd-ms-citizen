package it.gov.pagopa.bpd.citizen.connector.checkiban.exception;

import eu.sia.meda.exceptions.MedaDomainRuntimeException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class UnknowPSPException extends MedaDomainRuntimeException {

    public UnknowPSPException() {
        super("UNKNOW PSP", "psp.unknow.error", HttpStatus.NOT_IMPLEMENTED);
    }
}