package it.gov.pagopa.bpd.citizen.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@IdClass(CitizenRankingId.class)
@NoArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode", "awardPeriodId"}, callSuper = false)
@Table(name = "bpd_citizen_ranking")
public class CitizenRanking extends BaseEntity {

    //@Id
    //@Column(name = "id_n")
    //private Long id;

    @Id
    @Column(name = "fiscal_code_c")
    private String fiscalCode;

    @Id
    @Column(name = "award_period_id_n")
    private Long awardPeriodId;

    @Column(name = "ranking_n")
    private Long ranking;

}