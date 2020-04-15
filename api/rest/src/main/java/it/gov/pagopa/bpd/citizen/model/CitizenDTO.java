package it.gov.pagopa.bpd.citizen.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
public class CitizenDTO {

    @NotNull
    private ZonedDateTime timestampTc;
}
