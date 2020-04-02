package it.gov.pagopa.bpd.citizen.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.ZonedDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {

    @Column(name = "INSERT_DATE_D")
    private ZonedDateTime insertDate;

    @Column(name = "INSERT_USER_S")
    private String insertUser;

    @Column(name = "UPDATE_DATE_D")
    private ZonedDateTime updateDate;

    @Column(name = "UPDATE_USER_S")
    private String updateUser;

    @Column(name = "ENABLED_B")
    private boolean enabled = true;
}
