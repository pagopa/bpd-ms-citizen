package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.service.model.entity.Citizen;

import java.util.Optional;

public interface CitizenDAOService {

    Optional<Citizen> find(String fiscalCode);

    Citizen update(String fiscalCode, Citizen cz);

    void delete(String fiscalCode);

}
