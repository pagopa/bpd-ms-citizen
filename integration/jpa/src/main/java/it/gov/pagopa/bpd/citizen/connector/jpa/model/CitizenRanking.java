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
@EqualsAndHashCode(of = {"hpan", "fiscalCode", "awardPeriodId"}, callSuper = false)
@Table(name = "bpd_citizen_ranking")
public class CitizenRanking extends BaseEntity {

    @Id
    @Column(name = "hpan_s")
    private String hpan;

    @Id
    @Column(name = "fiscal_code_c")
    private String fiscalCode;

    @Id
    @Column(name = "award_period_id_n")
    private Long awardPeriodId;

    @Column(name = "cashback_n")
    private Long cashback;

    @Column(name = "transaction_n")
    private Long transactionNumber;

}
