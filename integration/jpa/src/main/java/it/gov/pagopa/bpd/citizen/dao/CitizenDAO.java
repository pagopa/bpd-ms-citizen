package it.gov.pagopa.bpd.citizen.dao;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.citizen.model.Citizen;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenDAO extends CrudJpaDAO<Citizen, String> {
}
