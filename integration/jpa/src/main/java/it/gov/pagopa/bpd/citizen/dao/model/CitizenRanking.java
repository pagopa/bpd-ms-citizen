package it.gov.pagopa.bpd.citizen.dao.model;

import it.gov.pagopa.bpd.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table(name = "bpd_citizen_ranking")
public class CitizenRanking extends BaseEntity {

    @Id
    @Column(name = "id_n")
    private Long id;

    @Column(name = "fiscal_code_c")
    private String fiscalCode;

    @Column(name = "award_period_id_n")
    private Long awardPeriodId;

    @Column(name = "ranking_n")
    private Long ranking;

}
