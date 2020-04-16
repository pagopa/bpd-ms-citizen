package it.gov.pagopa.bpd.citizen.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
public class CitizenDTO {

    @NotNull
    private ZonedDateTime timestampTc;
    @Size(max = 27)
    private String payoffInstr;
    @Size(max = 4)
    private String payoffInstrType;

}
