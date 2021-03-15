package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.common.util.Constants;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CitizenPatchDTO {

    @ApiModelProperty(value = "${swagger.citizen.payoffInstr}", required = true)
    @JsonProperty(required = true)
    @NotNull
//    @Size(max = 27)
//    @Pattern(regexp = Constants.IBAN_REGEX)
    private String payoffInstr;

    @ApiModelProperty(value = "${swagger.citizen.payoffInstrType}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private Citizen.PayoffInstrumentType payoffInstrType;

    @ApiModelProperty(value = "${swagger.citizen.accountHolderCF}", required = true)
    @JsonProperty(required = true)
    @NotNull
    @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
    private String accountHolderCF;

    @ApiModelProperty(value = "${swagger.citizen.accountHolderName}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private String accountHolderName;

    @ApiModelProperty(value = "${swagger.citizen.accountHolderSurname}", required = true)
    @JsonProperty(required = true)
    @NotNull
    private String accountHolderSurname;

    @ApiModelProperty(value = "${swagger.citizen.technicalAccountHolder}", required = false)
    @JsonProperty(required = false)
    private String technicalAccountHolder;

    @ApiModelProperty(value = "${swagger.citizen.issuerCardId}", required = false)
    @Size(max = 20)
    @JsonProperty(required = false)
    private String issuerCardId;

}
