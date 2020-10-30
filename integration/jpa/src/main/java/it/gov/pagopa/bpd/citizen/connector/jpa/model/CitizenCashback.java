package it.gov.pagopa.bpd.citizen.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@IdClass(CitizenCashbackCompositeKey.class)
@EqualsAndHashCode(of = {"fiscalCode", "awardPeriodId", "hpan"}, callSuper = false)
@Table(name = "bpd_citizen_ranking")
public class CitizenCashback extends BaseEntity {

    @Id
    @Column(name = "fiscal_code_c")
    private String fiscalCode;

    @Id
    @Column(name = "award_period_id_n")
    private Long awardPeriodId;

    @Id
    @Column(name = "hpan_s")
    private String hpan;

    @Column(name = "cashback_n")
    private BigDecimal totalCashback;

    @Column(name = "transaction_n")
    private Long transactionNumber;

}
