package it.gov.pagopa.bpd.citizen.model.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CitizenDTO {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private ZonedDateTime timestamp;
}
