package eu.sia.bpd.demo.service;

import eu.sia.bpd.demo.model.Citizen;

import java.util.Optional;

public interface CitizenDAOService {

    Citizen insert (Citizen cz);

    Optional<Citizen> find (String fiscalCode);

    Citizen update (Citizen cz);

    Citizen delete (String fiscalCode);


}
