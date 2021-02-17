package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MilestoneResource {

    @ApiModelProperty(value = "${swagger.citizen.idTrxPivot}", required = true)
    @JsonProperty(required = true)
    private String idTrxPivot;

    @ApiModelProperty(value = "${swagger.citizen.cashbackNorm}", required = true)
    @JsonProperty(required = true)
    private BigDecimal cashbackNorm;

    @ApiModelProperty(value = "${swagger.citizen.idTrxMinTransactionNumber}", required = true)
    @JsonProperty(required = true)
    private String idTrxMinTransactionNumber;

    @ApiModelProperty(value = "${swagger.citizen.totalCashback}", required = true)
    @JsonProperty(required = true)
    private BigDecimal totalCashback;

    @ApiModelProperty(value = "${swagger.citizen.maxCashback}", required = true)
    @JsonProperty(required = true)
    private BigDecimal maxCashback;
}
