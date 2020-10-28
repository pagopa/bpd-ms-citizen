package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CitizenCashbackResource {

    @ApiModelProperty(value = "${swagger.citizen.transactionNumber}", required = true)
    @JsonProperty(required = true)
    private Long transactionNumber;
    @ApiModelProperty(value = "${swagger.citizen.totalCashback}", required = true)
    @JsonProperty(required = true)
    private BigDecimal totalCashback;
}
