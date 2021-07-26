package it.gov.pagopa.bpd.citizen.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenEventRecord;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

public class CitizenEventRecordDAOTest extends BaseCrudJpaDAOTest<CitizenEventRecordDAO, CitizenEventRecord, String> {

    @Data
    private static class CitizenEventRecordCriteria implements CriteriaQuery<CitizenEventRecord> {
        private String fiscalCode;
    }


    @Autowired
    private CitizenEventRecordDAO citizenEventRecordDAO;


    @Override
    protected CriteriaQuery<? super CitizenEventRecord> getMatchAlreadySavedCriteria() {
        CitizenEventRecordDAOTest.CitizenEventRecordCriteria criteriaQuery =
                new CitizenEventRecordDAOTest.CitizenEventRecordCriteria();
        criteriaQuery.setFiscalCode(getStoredId());

        return criteriaQuery;
    }


    @Override
    protected CitizenEventRecordDAO getDao() {
        return citizenEventRecordDAO;
    }


    @Override
    protected void setId(CitizenEventRecord entity, String id) {
        entity.setFiscalCode(id);
    }


    @Override
    protected String getId(CitizenEventRecord entity) {
        return entity.getFiscalCode();
    }


    @Override
    protected void alterEntityToUpdate(CitizenEventRecord entity) {
        entity.setEventStatus(true);
    }


    @Override
    protected Function<Integer, String> idBuilderFn() {
        return (bias) -> "fiscalCode".toUpperCase() + bias;
    }


    @Override
    protected String getIdName() {
        return "fiscalCode";
    }
}