package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenCashback;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenCashbackCompositeKey;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitizenCashbackDAO extends CrudJpaDAO<CitizenCashback, CitizenCashbackCompositeKey> {

    @Query("SELECT cr FROM CitizenCashback cr WHERE fiscalCode = :fiscalCode AND awardPeriodId = :awardPeriod")
    List<CitizenCashback> getTotalCashback(
            String fiscalCode, Long awardPeriod);

    @Query("SELECT cr FROM CitizenCashback cr WHERE fiscalCode = :fiscalCode " +
            "AND awardPeriodId = :awardPeriod AND hpan = :hpan")
    List<CitizenCashback> getPaymentInstrumentCashback(
            String hpan, String fiscalCode, Long awardPeriod);
}
