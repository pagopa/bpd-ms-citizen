package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenTransaction;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class CitizenTransactionDAOImpl implements CitizenTransactionDAO {

    @PersistenceContext
    private EntityManager em;


    @Override
    public CitizenTransaction getTransactionDetails(String fiscalCode, Long awardPeriodId) {
        String QUERY_GET_TRANSACTION = "SELECT * FROM bpd_citizen.get_transaction_details('" + fiscalCode + "', " + awardPeriodId + ")";
        //noinspection unchecked
        return (CitizenTransaction) em.createNativeQuery(QUERY_GET_TRANSACTION, CitizenTransaction.class).getSingleResult();
    }
}
