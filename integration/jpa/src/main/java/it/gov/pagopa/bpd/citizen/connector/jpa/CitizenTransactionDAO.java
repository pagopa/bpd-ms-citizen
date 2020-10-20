package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenTransaction;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenTransactionDAO {

    CitizenTransaction getTransactionDetails(String fiscalCode, Long awardPeriodId);
}
