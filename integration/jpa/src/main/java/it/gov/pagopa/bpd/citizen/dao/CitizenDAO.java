package it.gov.pagopa.bpd.citizen.dao;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenDAO extends CrudJpaDAO<Citizen, String> {
}
