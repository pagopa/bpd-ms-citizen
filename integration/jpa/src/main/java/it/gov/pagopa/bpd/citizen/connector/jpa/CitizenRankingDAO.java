package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
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
            value = " select " +
                    " case " +
                    "   when cit.cit_fiscal_code is null then :fiscalCode " +
                    "   else cit.cit_fiscal_code " +
                    " end as fiscalCode, " +
                    " case " +
                    "   when cit.cit_ranking is null then count(1)+ 1 " +
                    "   else cit.cit_ranking " +
                    " end as ranking, " +
                    " count(1) as totalParticipants, " +
                    " max(bcr_out.transaction_n) as maxTrxNumber, " +
                    " case " +
                    "   when cit.cit_ranking_min is null then min(bcr_out.transaction_n) filter ( " +
                    "       where bcr_out.ranking_n = ( " +
                    "           select " +
                    "           distinct casecit.ranking_min_n " +
                    "           from " +
                    "           bpd_citizen_ranking casecit " +
                    "           where " +
                    "           casecit.award_period_id_n = bcr_out.award_period_id_n)) " +
                    "   else min(bcr_out.transaction_n) filter ( " +
                    "       where bcr_out.ranking_n = cit.cit_ranking_min) " +
                    " end as minTrxNumber, " +
                    " case " +
                    "   when cit.cit_transaction is null then 0 " +
                    "   else cit.cit_transaction " +
                    " end as trxNumber, " +
                    " bcr_out.award_period_id_n as awardPeriodId " +
                    " from " +
                    " bpd_citizen_ranking bcr_out " +
                    " left outer join ( " +
                    " select " +
                    " bcr.fiscal_code_c as cit_fiscal_code,  " +
                    " bcr.award_period_id_n as cit_award_period_id,  " +
                    " bcr.cashback_n as cit_cashback,  " +
                    " bcr.transaction_n as cit_transaction,  " +
                    " bcr.ranking_n as cit_ranking,  " +
                    " bcr.ranking_min_n as cit_ranking_min " +
                    " from " +
                    " bpd_citizen_ranking bcr " +
                    " where " +
                    " bcr.fiscal_code_c = :fiscalCode " +
                    " and (:awardPeriod = -1 or bcr.award_period_id_n = :awardPeriod) " +
                    " and bcr.enabled_b = true) cit on " +
                    " bcr_out.award_period_id_n = cit.cit_award_period_id " +
                    " where (:awardPeriod = -1 or bcr_out.award_period_id_n = :awardPeriod) " +
                    " and bcr_out.enabled_b = true " +
                    " group by " +
                    " cit.cit_fiscal_code, " +
                    " cit.cit_ranking, " +
                    " cit.cit_transaction, " +
                    " bcr_out.award_period_id_n, " +
                    " cit.cit_ranking_min ")
    List<CitizenTransactionConverter> getRanking(
            @Param("fiscalCode") String fiscalCode, @Param("awardPeriod") Long awardPeriod);


}