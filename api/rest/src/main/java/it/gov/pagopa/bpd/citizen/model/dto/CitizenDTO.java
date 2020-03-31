package it.gov.pagopa.bpd.citizen.model.dto;

import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CitizenDTO extends ReflectionDTO<Citizen> {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private ZonedDateTime timestamp;
}
