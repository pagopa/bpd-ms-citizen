package it.gov.pagopa.bpd.citizen.connector.jpa.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class CitizenTransaction implements Serializable {

    @Id
    @Column(name = "max_trx")
    Long maxTrx;

    @Id
    @Column(name = "min_trx")
    Long minTrx;

    @Id
    @Column(name = "total_trx")
    Long totalTrx;
}
