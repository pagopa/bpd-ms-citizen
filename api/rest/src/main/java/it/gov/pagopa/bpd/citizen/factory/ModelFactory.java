package it.gov.pagopa.bpd.citizen.factory;

import it.gov.pagopa.bpd.citizen.model.dto.DTO;

public interface ModelFactory<T extends DTO, U> {

    U createModel(T dto);

}
