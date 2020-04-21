package it.gov.pagopa.bpd.citizen.dao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Table(name = "bpd_file_storage")
public class FileStorage implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "start_date_t")
    private OffsetDateTime startDate;

    @Column(name = "end_date_t")
    private OffsetDateTime endDate;

    @Column(name = "type_t")
    private String type;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "file_clob")
    private byte[] file;

}
