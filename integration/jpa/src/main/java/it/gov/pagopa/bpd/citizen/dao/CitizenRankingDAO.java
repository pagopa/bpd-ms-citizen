package it.gov.pagopa.bpd.citizen.dao;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.citizen.dao.model.CitizenRanking;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenRankingDAO extends CrudJpaDAO<CitizenRanking, Long> {

    CitizenRanking findByFiscalCodeAndAwardPeriodId(
            String fiscalCode, Long awardPeriodId);
}