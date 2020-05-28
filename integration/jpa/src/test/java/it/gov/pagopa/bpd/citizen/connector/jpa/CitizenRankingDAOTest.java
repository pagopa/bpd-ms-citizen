package it.gov.pagopa.bpd.citizen.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

public class CitizenRankingDAOTest extends BaseCrudJpaDAOTest<CitizenRankingDAO, CitizenRanking, Long> {

    @Autowired
    private CitizenRankingDAO citizenRankingDAO;

    @Override
    protected CriteriaQuery<? super CitizenRanking> getMatchAlreadySavedCriteria() {
        CitizenRankingDAOTest.CitizenRankingCriteria criteriaQuery = new CitizenRankingDAOTest.CitizenRankingCriteria();
        criteriaQuery.setId(getStoredId());

        return criteriaQuery;
    }

    @Override
    protected CitizenRankingDAO getDao() {
        return citizenRankingDAO;
    }

    @Override
    protected void setId(CitizenRanking entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long getId(CitizenRanking entity) {
        return entity.getId();
    }

    @Override
    protected void alterEntityToUpdate(CitizenRanking entity) {
        entity.setFiscalCode("changed");
    }

    @Override
    protected Function<Integer, Long> idBuilderFn() {
        return (bias) -> bias.longValue();
    }

    @Override
    protected String getIdName() {
        return "id";
    }

    @Data
    private static class CitizenRankingCriteria implements CriteriaQuery<CitizenRanking> {
        private Long id;
    }
}