package it.gov.pagopa.bpd.citizen.model;

import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CitizenPatchDTO {

    @NotNull
    @Size(max = 27)
    private String payoffInstr;
    @NotNull
    private Citizen.PayoffInstrumentType payoffInstrType;

}
