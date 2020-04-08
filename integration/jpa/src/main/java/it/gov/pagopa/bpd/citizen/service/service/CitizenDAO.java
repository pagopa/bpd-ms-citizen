package it.gov.pagopa.bpd.citizen.service.service;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.citizen.service.service.model.entity.Citizen;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenDAO extends CrudJpaDAO<Citizen, String> {
}
