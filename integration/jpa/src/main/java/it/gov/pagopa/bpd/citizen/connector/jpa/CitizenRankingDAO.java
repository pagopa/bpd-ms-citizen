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


    @Query(nativeQuery = true,
            value = " select tmp.fiscalCode, " +
                    " tmp.ranking, " +
                    " tmp.totalParticipants, " +
                    " tmp.maxTrxNumber, " +
                    " tmp.minTrxNumber, " +
                    " tmp.trxNumber, " +
                    " tmp.awardPeriodId " +
                    " from ( " +
                    "   select " +
                    "   case " +
                    "       when cit.cit_fiscal_code is null then :fiscalCode " +
                    "       else cit.cit_fiscal_code " +
                    "   end as fiscalCode, " +
                    "   case " +
                    "       when cit.cit_ranking is null then count(1)+ 1 " +
                    "       else cit.cit_ranking " +
                    "   end as ranking, " +
                    "   count(1) as totalParticipants, " +
                    "    max(bcr_out.transaction_n) as maxTrxNumber, " +
                    "    case " +
                    "       when cit.cit_ranking_min is null then min(bcr_out.transaction_n) filter ( " +
                    "           where bcr_out.ranking_n = ( " +
                    "               select " +
                    "               distinct casecit.ranking_min_n " +
                    "               from " +
                    "               bpd_citizen_ranking casecit " +
                    "               where " +
                    "               casecit.award_period_id_n = bcr_out.award_period_id_n)) " +
                    "       else min(bcr_out.transaction_n) filter ( " +
                    "           where bcr_out.ranking_n = cit.cit_ranking_min) " +
                    "   end as minTrxNumber, " +
                    "   case " +
                    "     when cit.cit_transaction is null then 0 " +
                    "      else cit.cit_transaction " +
                    "    end as trxNumber, " +
                    "    bcr_out.award_period_id_n as awardPeriodId " +
                    "    from " +
                    "    bpd_citizen_ranking bcr_out " +
                    "    inner join bpd_citizen bc_out on bcr_out.fiscal_code_c = bc_out.fiscal_code_s " +
                    "    left outer join ( " +
                    "    select " +
                    "    bcr.fiscal_code_c as cit_fiscal_code,  " +
                    "    bcr.award_period_id_n as cit_award_period_id,  " +
                    "    case " +
                    "     when bcr.cashback_n > bcr.max_cashback_n then bcr.max_cashback_n " +
                    "      else bcr.cashback_n " +
                    "    end as cit_cashback,  " +
                    "    bcr.transaction_n as cit_transaction,  " +
                    "    bcr.ranking_n as cit_ranking,  " +
                    "    bcr.ranking_min_n as cit_ranking_min " +
                    "    from " +
                    "    bpd_citizen_ranking bcr " +
                    "    inner join bpd_citizen bc on bcr.fiscal_code_c = bc.fiscal_code_s " +
                    "    where " +
                    "    bcr.fiscal_code_c = :fiscalCode " +
                    "    and (:awardPeriod = -1 or bcr.award_period_id_n = :awardPeriod) " +
                    "    and bc.enabled_b = true) cit on " +
                    "    bcr_out.award_period_id_n = cit.cit_award_period_id " +
                    "    where (:awardPeriod = -1 or bcr_out.award_period_id_n = :awardPeriod) " +
                    "    and bc_out.enabled_b = true " +
                    "    group by " +
                    "    cit.cit_fiscal_code, " +
                    "    cit.cit_ranking, " +
                    "    cit.cit_transaction, " +
                    "    bcr_out.award_period_id_n, " +
                    "    cit.cit_ranking_min " +
                    "   union " +
                    "   select :fiscalCode as fiscalCode, " +
                    "    0 as ranking, " +
                    "   0 as totalParticipants, " +
                    "    0 as maxTrxNumber, " +
                    "   0 as minTrxNumber, " +
                    "    0 as trxNumber, " +
                    "   :awardPeriod as awardPeriodId) as tmp " +
                    " order by tmp.ranking desc limit 1 ")
    List<CitizenTransactionConverter> getRanking(
            @Param("fiscalCode") String fiscalCode, @Param("awardPeriod") Long awardPeriod);

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