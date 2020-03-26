package eu.sia.bpd.demo.integration;

import eu.sia.bpd.demo.model.Citizen;
import eu.sia.meda.connector.jpa.JPAConnector;

public interface CitizenDAO extends JPAConnector<Citizen, String> {
}
