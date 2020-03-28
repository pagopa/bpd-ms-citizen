package it.gov.pagopa.bpd.citizen.model.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@Data
@EqualsAndHashCode(of = "fiscalCode", callSuper = false)
public class CitizenResource {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private Date timestamp;

}
