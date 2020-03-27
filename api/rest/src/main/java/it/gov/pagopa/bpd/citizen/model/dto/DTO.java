package it.gov.pagopa.bpd.citizen.model.dto;

import java.io.Serializable;

public interface DTO<T extends Serializable> {

    T toEntity();

}
