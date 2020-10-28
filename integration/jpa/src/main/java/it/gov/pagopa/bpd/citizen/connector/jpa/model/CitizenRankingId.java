package it.gov.pagopa.bpd.citizen.connector.jpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CitizenRankingId implements Serializable {

    private String hpan;
    private String fiscalCode;
    private Long awardPeriodId;

}
