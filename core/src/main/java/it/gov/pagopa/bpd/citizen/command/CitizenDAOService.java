package it.gov.pagopa.bpd.citizen.command;

import it.gov.pagopa.bpd.citizen.model.entity.Citizen;

import java.util.Optional;

public interface CitizenDAOService {

    Citizen insert(Citizen cz);

    Optional<Citizen> find(String fiscalCode);

    Citizen update(Citizen cz);

    void delete(String fiscalCode);

}
