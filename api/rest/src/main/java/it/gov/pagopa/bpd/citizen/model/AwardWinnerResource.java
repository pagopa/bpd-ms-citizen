package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;


@Data
@EqualsAndHashCode(of = "fiscalCode", callSuper = false)
public class AwardWinnerResource {

//    @ApiModelProperty(value = "${swagger.citizen.award.winner.fiscalCode}", required = true)
//    @JsonProperty(required = true)
//    private String fiscalCode;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.awardPeriodId}", required = true)
    @JsonProperty(required = true)
    private Long awardPeriodId;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.awardPeriodStart}", required = true)
    @JsonProperty(required = true)
    private LocalDate awardPeriodStart;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.awardPeriodEnd}", required = true)
    @JsonProperty(required = true)
    private LocalDate awardPeriodEnd;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.cashback}", required = true)
    @JsonProperty(required = true)
    private BigDecimal cashback;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.jackpot}", required = false)
    @JsonProperty(required = false)
    private BigDecimal jackpot;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.amount}", required = true)
    @JsonProperty(required = true)
    private BigDecimal amount;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.payoffInstr}", required = true)
    @JsonProperty(required = true)
    private String payoffInstr;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.accountHolderFiscalCode}", required = true)
    @JsonProperty(required = true)
    private String accountHolderFiscalCode;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.accountHolderName}", required = true)
    @JsonProperty(required = true)
    private String accountHolderName;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.accountHolderSurname}", required = true)
    @JsonProperty(required = true)
    private String accountHolderSurname;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.technicalAccountHolder}", required = false)
    @JsonProperty(required = false)
    private String technicalAccountHolder;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.status}", required = true)
    @JsonProperty(required = true)
    private String status;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.result}", required = false)
    @JsonProperty(required = false)
    private String result;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.resultReason}", required = false)
    @JsonProperty(required = false)
    private String resultReason;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.cro}", required = false)
    @JsonProperty(required = false)
    private String cro;
    @ApiModelProperty(value = "${swagger.citizen.award.winner.executionDate}", required = false)
    @JsonProperty(required = false)
    private LocalDate executionDate;

}
