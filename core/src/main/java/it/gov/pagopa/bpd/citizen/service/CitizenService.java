package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.dao.model.Citizen;

import java.util.Optional;

public interface CitizenService {

    Optional<Citizen> find(String fiscalCode);

    Citizen update(String fiscalCode, Citizen cz);

    Citizen patch(String fiscalCode, Citizen cz);

    void delete(String fiscalCode);

}
