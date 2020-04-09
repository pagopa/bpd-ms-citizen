package it.gov.pagopa.bpd.citizen.service.factory;

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
