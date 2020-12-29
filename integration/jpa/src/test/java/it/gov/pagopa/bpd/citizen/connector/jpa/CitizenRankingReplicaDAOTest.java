package it.gov.pagopa.bpd.citizen.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

public class CitizenRankingReplicaDAOTest extends BaseCrudJpaDAOTest<CitizenRankingReplicaDAO, CitizenRanking, CitizenRankingId> {

    @Autowired
    private CitizenRankingReplicaDAO citizenRankingReplicaDAO;

    @Override
    protected CriteriaQuery<? super CitizenRanking> getMatchAlreadySavedCriteria() {
        CitizenRankingReplicaDAOTest.CitizenRankingCriteria criteriaQuery = new CitizenRankingReplicaDAOTest.CitizenRankingCriteria();
        criteriaQuery.setFiscalCode(getStoredId().getFiscalCode());
        criteriaQuery.setAwardPeriodId(getStoredId().getAwardPeriodId());


        return criteriaQuery;
    }

    @Override
    protected CitizenRankingReplicaDAO getDao() {
        return citizenRankingReplicaDAO;
    }

    @Override
    protected void setId(CitizenRanking entity, CitizenRankingId id)
    {
        entity.setAwardPeriodId(id.getAwardPeriodId());
        entity.setFiscalCode(id.getFiscalCode());
    }

    @Override
    protected CitizenRankingId getId(CitizenRanking entity) {
        return new CitizenRankingId(entity.getFiscalCode(), entity.getAwardPeriodId());
    }

    @Override
    protected void alterEntityToUpdate(CitizenRanking entity) {
        entity.setRanking(1002L);
    }

    @Override
    protected Function<Integer, CitizenRankingId> idBuilderFn() {
        return (bias) ->
                new CitizenRankingId(String.valueOf(bias.longValue()), bias.longValue());
    }

    @Override
    protected String getIdName() {
        return "fiscalCode";
    }

    @Data
    private static class CitizenRankingCriteria implements CriteriaQuery<CitizenRanking> {
        private String fiscalCode;
        private Long awardPeriodId;
    }
}