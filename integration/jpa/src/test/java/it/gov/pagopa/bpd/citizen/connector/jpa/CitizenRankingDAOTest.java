package it.gov.pagopa.bpd.citizen.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

public class CitizenRankingDAOTest extends BaseCrudJpaDAOTest<CitizenRankingDAO, CitizenRanking, CitizenRankingId> {

    @Autowired
    private CitizenRankingDAO citizenRankingDAO;

    @Override
    protected CriteriaQuery<? super CitizenRanking> getMatchAlreadySavedCriteria() {
        CitizenRankingDAOTest.CitizenRankingCriteria criteriaQuery = new CitizenRankingDAOTest.CitizenRankingCriteria();
        criteriaQuery.setHpan(getStoredId().getHpan());
        criteriaQuery.setFiscalCode(getStoredId().getFiscalCode());
        criteriaQuery.setAwardPeriodId(getStoredId().getAwardPeriodId());


        return criteriaQuery;
    }

    @Override
    protected CitizenRankingDAO getDao() {
        return citizenRankingDAO;
    }

    @Override
    protected void setId(CitizenRanking entity, CitizenRankingId id)
    {
        entity.setHpan(id.getHpan());
        entity.setAwardPeriodId(id.getAwardPeriodId());
        entity.setFiscalCode(id.getFiscalCode());
    }

    @Override
    protected CitizenRankingId getId(CitizenRanking entity) {
        return new CitizenRankingId(
                entity.getHpan(),
                entity.getFiscalCode(),
                entity.getAwardPeriodId());
    }

    @Override
    protected void alterEntityToUpdate(CitizenRanking entity) {
        entity.setCashback(1002L);
    }

    @Override
    protected Function<Integer, CitizenRankingId> idBuilderFn() {
        return (bias) ->
                new CitizenRankingId(
                        String.valueOf(bias),
                        String.valueOf(bias),
                        bias.longValue());
    }

    @Override
    protected String getIdName() {
        return "fiscalCode";
    }

    @Data
    private static class CitizenRankingCriteria implements CriteriaQuery<CitizenRanking> {
        private String hpan;
        private String fiscalCode;
        private Long awardPeriodId;
    }
}