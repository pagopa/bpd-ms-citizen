package it.gov.pagopa.bpd.citizen.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenCashback;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenCashbackCompositeKey;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.function.Function;

public class CitizenCashbackDAOTest extends BaseCrudJpaDAOTest<CitizenCashbackDAO, CitizenCashback, CitizenCashbackCompositeKey> {

    @Autowired
    private CitizenCashbackDAO citizenCashbackDAO;

    @Override
    protected CriteriaQuery<? super CitizenCashback> getMatchAlreadySavedCriteria() {
        CitizenCashbackDAOTest.CitizenCashbackCriteria criteriaQuery = new CitizenCashbackDAOTest.CitizenCashbackCriteria();
        criteriaQuery.setHpan(getStoredId().getHpan());
        criteriaQuery.setFiscalCode(getStoredId().getFiscalCode());
        criteriaQuery.setAwardPeriodId(getStoredId().getAwardPeriodId());

        return criteriaQuery;
    }

    @Override
    protected CitizenCashbackDAO getDao() {
        return citizenCashbackDAO;
    }

    @Override
    protected void setId(CitizenCashback entity, CitizenCashbackCompositeKey id) {
        entity.setHpan(id.getHpan());
        entity.setFiscalCode(id.getFiscalCode());
        entity.setAwardPeriodId(id.getAwardPeriodId());
    }

    @Override
    protected CitizenCashbackCompositeKey getId(CitizenCashback entity) {
        return new CitizenCashbackCompositeKey(
                entity.getHpan(),
                entity.getFiscalCode(),
                entity.getAwardPeriodId());
    }

    @Override
    protected void alterEntityToUpdate(CitizenCashback entity) {
        entity.setTotalCashback(new BigDecimal(100));
        entity.setTransactionNumber(1L);
    }

    @Override
    protected Function<Integer, CitizenCashbackCompositeKey> idBuilderFn() {
        return (bias) ->
                new CitizenCashbackCompositeKey(
                        String.valueOf(bias),
                        String.valueOf(bias),
                        bias.longValue());
    }

    @Override
    protected String getIdName() {
        return "fiscalCode";
    }

    @Data
    private static class CitizenCashbackCriteria implements CriteriaQuery<CitizenCashback> {
        private String hpan;
        private String fiscalCode;
        private Long awardPeriodId;
    }
}
