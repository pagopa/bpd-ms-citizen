package it.gov.pagopa.bpd.citizen.model.dto;

import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import lombok.Data;

import java.util.Date;

@Data
public class CitizenDTO extends ReflectionDTO<Citizen> {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private Date timestamp;
}
