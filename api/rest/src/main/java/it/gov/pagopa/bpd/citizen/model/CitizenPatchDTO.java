package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CitizenPatchDTO {

    @ApiModelProperty(value = "${swagger.citizen.payoffInstr}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @Size(max = 27)
    private String payoffInstr;
    @ApiModelProperty(value = "${swagger.citizen.payoffInstrType}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Citizen.PayoffInstrumentType payoffInstrType;

}
