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
            value = " select tmp.fiscalCode, " +
                    "   coalesce(tmp.ranking, tmp.totalParticipants+1) as ranking, " +
                    "   tmp.totalParticipants, " +
                    "   tmp.maxTrxNumber, " +
                    "   tmp.minTrxNumber, " +
                    "   coalesce(tmp.trxNumber,0) as trxNumber, " +
                    "   tmp.awardPeriodId " +
                    " from ( " +
                    "   select " +
                    " coalesce(cit.cit_fiscal_code, :fiscalCode) as fiscalCode, " +
                    " cit.cit_transaction as trxNumber, " +
                    " cit.cit_ranking as ranking, " +
                    " count(1) as totalParticipants, " +
                    " bcr_out.award_period_id_n as awardPeriodId, " +
                    " max(bcr_out.transaction_n) as maxTrxNumber, " +
                    " min(bcr_out.transaction_n) filter(where bcr_out.ranking_n=bcr_out.ranking_min_n) as minTrxNumber" +
                    " from bpd_citizen.bpd_citizen_ranking bcr_out" +
                    " left outer join ( " +
                    " select " +
                    "  bcr.fiscal_code_c as cit_fiscal_code," +
                    "  bcr.award_period_id_n as cit_award_period_id," +
                    "  case " +
                    "   when bcr.cashback_n > bcr.max_cashback_n then bcr.max_cashback_n " +
                    "   else bcr.cashback_n " +
                    "  end as cit_cashback,  " +
                    "  bcr.transaction_n as cit_transaction,  " +
                    "  bcr.ranking_n as cit_ranking " +
                    " from bpd_citizen.bpd_citizen_ranking bcr " +
                    " inner join bpd_citizen.bpd_citizen bc on bcr.fiscal_code_c=bc.fiscal_code_s " +
                    " where bcr.fiscal_code_c= :fiscalCode " +
                    " and bc.enabled_b=true " +
                    " and (:awardPeriod = -1 or bcr.award_period_id_n= :awardPeriod) " +
                    " ) as cit on bcr_out.award_period_id_n=cit.cit_award_period_id " +
                    " where (:awardPeriod = -1 or bcr_out.award_period_id_n= :awardPeriod) " +
                    " group by bcr_out.award_period_id_n, " +
                    " coalesce(cit.cit_fiscal_code, :fiscalCode), " +
                    " cit.cit_transaction, " +
                    " cit.cit_ranking " +
                    " ) tmp")
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


}