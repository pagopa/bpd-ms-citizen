package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class CitizenRankingResource {

    @ApiModelProperty(value = "${swagger.citizen.ranking}", required = true)
    @JsonProperty(required = true)
    private BigDecimal totalCashback;
    @ApiModelProperty(value = "${swagger.citizen.totalParticipants}", required = true)
    @JsonProperty(required = true)
    private Long totalParticipants;
    @ApiModelProperty(value = "${swagger.citizen.maxTransactionNumber}", required = true)
    @JsonProperty(required = true)
    private Long maxTransactionNumber;
    @ApiModelProperty(value = "${swagger.citizen.minTransactionNumber}", required = true)
    @JsonProperty(required = true)
    private Long minTransactionNumber;
    @ApiModelProperty(value = "${swagger.citizen.transactionNumber}", required = true)
    @JsonProperty(required = true)
    private Long transactionNumber;
    @ApiModelProperty(value = "${swagger.citizen.awardPeriodId}", required = true)
    @JsonProperty(required = true)
    private Long awardPeriodId;

}
