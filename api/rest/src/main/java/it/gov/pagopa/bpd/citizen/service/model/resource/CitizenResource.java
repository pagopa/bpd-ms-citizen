package it.gov.pagopa.bpd.citizen.service.model.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;


@Data
@EqualsAndHashCode(of = "fiscalCode", callSuper = false)
public class CitizenResource {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private ZonedDateTime timestampTc;

}
