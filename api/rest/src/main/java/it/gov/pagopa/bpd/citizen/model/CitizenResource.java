package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(of = "fiscalCode", callSuper = false)
public class CitizenResource {

    @ApiModelProperty(value = "${swagger.citizen.fiscalCode}", required = true)
    @JsonProperty(required = true)
    private String fiscalCode;
    @ApiModelProperty(value = "${swagger.citizen.payoffInstr}", required = true)
    @JsonProperty(required = true)
    private String payoffInstr;
    @ApiModelProperty(value = "${swagger.citizen.payoffInstrType}", required = true)
    @JsonProperty(required = true)
    private String payoffInstrType;
    @ApiModelProperty(value = "${swagger.citizen.timestampTC}", required = true)
    @JsonProperty(required = true)
    private OffsetDateTime timestampTC;
    @ApiModelProperty(value = "${swagger.citizen.enabled}", required = true)
    @JsonProperty(required = true)
    private boolean enabled;
    @ApiModelProperty(value = "${swagger.citizen.technicalAccount}")
    private String technicalAccount;
    @ApiModelProperty(value = "${swagger.citizen.technicalAccountHolder}")
    private String technicalAccountHolder;
    @ApiModelProperty(value = "${swagger.citizen.issuerCardId}")
    private String issuerCardId;
}