package it.gov.pagopa.bpd.citizen.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
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

    @Column(name = "check_instr_status_s")
    private String checkInstrStatus;

    @Column(name = "account_holder_cf_s")
    private String accountHolderCF;

    @Column(name = "account_holder_name_s")
    private String accountHolderName;

    @Column(name = "account_holder_surname_s")
    private String accountHolderSurname;

}
