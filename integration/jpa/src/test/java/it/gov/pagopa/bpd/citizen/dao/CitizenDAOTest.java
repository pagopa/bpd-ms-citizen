package it.gov.pagopa.bpd.citizen.dao;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.common.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;

public class CitizenDAOTest extends BaseCrudJpaDAOTest<CitizenDAO, Citizen, String> {

    @Data
    private static class CitizenCriteria implements CriteriaQuery<Citizen> {
        private String fiscalCode;
    }


    @Autowired
    private CitizenDAO citizenDAO;


    @Override
    protected CriteriaQuery<? super Citizen> getMatchAlreadySavedCriteria() {
        CitizenDAOTest.CitizenCriteria criteriaQuery = new CitizenDAOTest.CitizenCriteria();
        criteriaQuery.setFiscalCode(getStoredId());

        return criteriaQuery;
    }


    @Override
    protected CitizenDAO getDao() {
        return citizenDAO;
    }


    @Override
    protected void setId(Citizen entity, String id) {
        entity.setFiscalCode(id);
    }


    @Override
    protected String getId(Citizen entity) {
        return entity.getFiscalCode();
    }


    @Override
    protected void alterEntityToUpdate(Citizen entity) {
        entity.setPayoffInstr("changed");
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