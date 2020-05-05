package it.gov.pagopa.bpd.citizen.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class CitizenRankingResource {

    @ApiModelProperty(value = "${swagger.citizen.ranking}", required = true)
    @JsonProperty(required = true)
    private Long ranking;
    @ApiModelProperty(value = "${swagger.citizen.attendeesNumber}", required = true)
    @JsonProperty(required = true)
    private Long attendeesNumber;

}
