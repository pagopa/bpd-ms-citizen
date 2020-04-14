package it.gov.pagopa.bpd.citizen.model;

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
    private boolean enabled;

}
