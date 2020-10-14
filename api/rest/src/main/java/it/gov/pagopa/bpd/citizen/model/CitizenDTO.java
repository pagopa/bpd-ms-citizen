package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class CitizenDTO {

    @ApiModelProperty(value = "${swagger.citizen.timestampTC}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private OffsetDateTime timestampTC;

}
