package eu.sia.bpd.demo.integration;

import eu.sia.bpd.demo.model.Citizen;
import eu.sia.meda.connector.jpa.JPAConnectorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
class CitizenDAOImpl extends JPAConnectorImpl<Citizen, String> implements CitizenDAO {

    @Autowired
    public CitizenDAOImpl(EntityManager em) {
        super(Citizen.class, em);
    }
}
