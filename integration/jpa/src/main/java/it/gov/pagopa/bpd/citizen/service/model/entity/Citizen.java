package it.gov.pagopa.bpd.citizen.service.model.entity;

import it.gov.pagopa.bpd.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
@Table(name = "bpd_citizen")
public class Citizen extends BaseEntity {

    @Id
    @Column(name = "fiscal_code_s")
    private String fiscalCode;

    @Column(name = "payoff_instr_s")
    private String payoffInstr;

    @Column(name = "payoff_instr_type_c")
    private String payoffInstrType;

    @Column(name = "timestamp_tc_t")
    private ZonedDateTime timestampTc;

}
