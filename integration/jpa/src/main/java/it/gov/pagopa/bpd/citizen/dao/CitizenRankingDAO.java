package it.gov.pagopa.bpd.citizen.dao;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.citizen.dao.model.CitizenRanking;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenRankingDAO extends CrudJpaDAO<CitizenRanking, Long> {

    CitizenRanking findByFiscalCodeAndAwardPeriodId(
            String fiscalCode, Long awardPeriodId);
}