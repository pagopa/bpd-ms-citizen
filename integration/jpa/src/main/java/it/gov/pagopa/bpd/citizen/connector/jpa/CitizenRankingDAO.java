package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenRankingDAO extends CrudJpaDAO<CitizenRanking, CitizenRankingId> {

    @Modifying
    @Query("update CitizenRanking " +
            "set updateDate = CURRENT_TIMESTAMP," +
            "updateUser = :fiscalCode, " +
            "totalCashback = 0, " +
            "transactionNumber = 0, " +
            "ranking = null, " +
            "rankingMinRequired = null, " +
            "maxTotalCashback = null, " +
            "enabled = false, " +
            "idTrxPivot = null, " +
            "cashbackNormPivot = 0, " +
            "idTrxMinTransactionNumber = null " +
            "where fiscalCode = :fiscalCode ")
    void deactivateCitizenRankingByFiscalCode(@Param("fiscalCode") String fiscalCode);
}