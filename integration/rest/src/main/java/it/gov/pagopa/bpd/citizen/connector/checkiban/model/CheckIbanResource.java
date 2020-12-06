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

    @Override
    public String toString(){
        return "{'status': '" + status
                +"', 'errors': " + ((errors!=null && !errors.isEmpty()) ? errors.toString() : "null")
                +", 'validationStatus': '" + ((payload!=null) ? payload.getValidationStatus() : "null") +"'}";
    }
}