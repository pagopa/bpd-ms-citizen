package eu.sia.bpd.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
@Table(name = "bpd_citizen", schema = "\"BPD_TEST\"")
public class Citizen implements Serializable {

    @Id
    @Column(name = "fiscal_code_c")
    private String fiscalCode;

    @Column(name = "payoff_instr_c")
    private String payoffInstr;

    @Column(name = "payoff_instr_type_c")
    private String payoffInstrType;

    @Column(name = "timestamp_t")
    private Date timestamp;

}
