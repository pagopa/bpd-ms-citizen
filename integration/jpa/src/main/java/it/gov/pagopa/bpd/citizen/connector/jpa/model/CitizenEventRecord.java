package it.gov.pagopa.bpd.citizen.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
@Table(name = "bpd_citizen_event_record")
public class CitizenEventRecord extends BaseEntity {

    @Id
    @Column(name = "fiscal_code_s")
    private String fiscalCode;

    @Column(name = "event_timestamp_t")
    private OffsetDateTime eventTimestamp;

    @Column(name = "event_status_b")
    private Boolean eventStatus;

    @Column(name = "sent_timestamp_t")
    private OffsetDateTime sentTimestamp;

}
