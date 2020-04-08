package it.gov.pagopa.bpd.citizen.service.service.factory;

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
