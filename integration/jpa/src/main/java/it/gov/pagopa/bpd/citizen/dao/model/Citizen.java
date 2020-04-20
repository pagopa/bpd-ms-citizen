package it.gov.pagopa.bpd.citizen.dao.model;

import it.gov.pagopa.bpd.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
@Table(name = "bpd_citizen")
public class Citizen extends BaseEntity {

    @Column(name = "payoff_instr_type_c")
    @Enumerated(EnumType.STRING)
    private PayoffInstrumentType payoffInstrType;

    @Id
    @Column(name = "fiscal_code_s")
    private String fiscalCode;

    @Column(name = "payoff_instr_s")
    private String payoffInstr;

    @Column(name = "timestamp_tc_t")
    private OffsetDateTime timestampTC;

    public enum PayoffInstrumentType {
        IBAN
    }

}
