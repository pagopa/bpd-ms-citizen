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

    @Query(nativeQuery = true,
            value = "select" +
                    " bc.fiscal_code_s as fiscalCode," +
                    "     bcr.ranking_n as ranking," +
                    "     bre.total_participants as totalParticipants," +
                    "     bre.max_transaction_n as maxTrxNumber," +
                    "     bre.min_transaction_n as minTrxNumber," +
                    "     bcr.transaction_n as trxNumber," +
                    "     bre.award_period_id_n as awardPeriodId" +
                    " from" +
                    "     bpd_citizen bc" +
                    " cross join bpd_ranking_ext bre" +
                    " left outer join bpd_citizen_ranking bcr on" +
                    "     bc.fiscal_code_s = bcr.fiscal_code_c" +
                    "     and bcr.award_period_id_n = bre.award_period_id_n" +
                    " where" +
                    "     bc.fiscal_code_s = :fiscalCode" +
                    "     and bc.enabled_b = true" +
                    "     and bre.award_period_id_n = :awardPeriod")
    List<CitizenTransactionConverter> getRanking(@Param("fiscalCode") String fiscalCode,
                                                 @Param("awardPeriod") Long awardPeriod);

    @Query(nativeQuery = true,
            value = "select" +
                    " bc.fiscal_code_s as fiscalCode," +
                    "     bcr.ranking_n as ranking," +
                    "     bre.total_participants as totalParticipants," +
                    "     bre.max_transaction_n as maxTrxNumber," +
                    "     bre.min_transaction_n as minTrxNumber," +
                    "     bcr.transaction_n as trxNumber," +
                    "     bre.award_period_id_n as awardPeriodId" +
                    " from" +
                    "     bpd_citizen bc" +
                    " cross join bpd_ranking_ext bre" +
                    " left outer join bpd_citizen_ranking bcr on" +
                    "     bc.fiscal_code_s = bcr.fiscal_code_c" +
                    "     and bcr.award_period_id_n = bre.award_period_id_n" +
                    " where" +
                    "     bc.fiscal_code_s = :fiscalCode" +
                    "     and bc.enabled_b = true")
    List<CitizenTransactionConverter> getRanking(@Param("fiscalCode") String fiscalCode);


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