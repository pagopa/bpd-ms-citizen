package it.gov.pagopa.bpd.citizen.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class CitizenDTO {

    @NotNull
    private OffsetDateTime timestampTC;

}
