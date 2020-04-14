package it.gov.pagopa.bpd.citizen.factory;

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
