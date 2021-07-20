package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenDAO extends CrudJpaDAO<Citizen, String> {

    @Override
    Citizen save(@NotNull Citizen entity);


}
