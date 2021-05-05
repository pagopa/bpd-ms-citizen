package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.AwardWinner;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.common.connector.jpa.ReadOnlyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Data Access Object to manage all CRUD operations to the database
 */
@ReadOnlyRepository
public interface AwardWinnerReplicaDAO extends CrudJpaDAO<AwardWinner, Long> {

    @Query(nativeQuery = true, value="SELECT * FROM bpd_award_winner aw " +
            "WHERE fiscal_code_s= :fiscalCode " +
            "AND (:awardPeriodId = -1 OR award_period_id_n = :awardPeriodId) " +
            "AND enabled_b IS TRUE")
    List<AwardWinner> findByFiscalCodeAndAwardPeriod(@Param("fiscalCode") String fiscalCode,
                                                     @Param("awardPeriodId") Long awardPeriodId);

}