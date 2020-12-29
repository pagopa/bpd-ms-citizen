package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenRankingDAO extends CrudJpaDAO<CitizenRanking, CitizenRankingId> {

    @Query(nativeQuery = true, value = "select * " +
            "from bpd_citizen_ranking bcr " +
            "inner join bpd_citizen bc on bc.fiscal_code_s = bcr.fiscal_code_c " +
            "where bcr.fiscal_code_c = :fiscalCode " +
            "and bcr.award_period_id_n = :awardPeriod " +
            "and bc.enabled_b = true")
    Optional<CitizenRanking> getByIdIfCitizenIsEnabled(@Param("fiscalCode") String fiscalCode,
                                                       @Param("awardPeriod") Long awardPeriod);

    @Modifying
    @Query("update CitizenRanking " +
            "set updateDate = CURRENT_TIMESTAMP," +
            "updateUser = :fiscalCode, " +
            "totalCashback = 0, " +
            "transactionNumber = 0, " +
            "ranking = null, " +
            "rankingMinRequired = null, " +
            "maxTotalCashback = null, " +
            "enabled = false " +
            "where fiscalCode = :fiscalCode ")
    void deactivateCitizenRankingByFiscalCode(@Param("fiscalCode") String fiscalCode);
}