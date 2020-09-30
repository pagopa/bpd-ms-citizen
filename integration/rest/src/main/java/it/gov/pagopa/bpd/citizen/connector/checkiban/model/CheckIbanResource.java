package it.gov.pagopa.bpd.citizen.connector.checkiban.model;

import java.util.List;

import lombok.Data;

@Data
public class CheckIbanResource{

    private String status;
    private List errors;
    private Payload payload;

    @Data
    public class Payload{
        private String validationStatus;

    }
}