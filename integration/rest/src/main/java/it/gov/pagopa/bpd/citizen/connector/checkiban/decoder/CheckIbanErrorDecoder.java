package it.gov.pagopa.bpd.citizen.connector.checkiban.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.InvalidIbanException;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.UnknowPSPException;

public class CheckIbanErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        switch (response.status()){
            case 400:
                return new InvalidIbanException();
            case 501:
                return new UnknowPSPException();
            default:
                return new Exception("Generic error");
        }
    }


}