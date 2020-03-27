package it.gov.pagopa.bpd.citizen;

import eu.sia.meda.connector.jpa.JPAConnector;
import it.gov.pagopa.bpd.citizen.model.entity.Citizen;

public interface CitizenDAO extends JPAConnector<Citizen, String> {
}
