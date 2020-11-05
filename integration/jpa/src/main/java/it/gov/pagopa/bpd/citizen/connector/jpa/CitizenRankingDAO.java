package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenRankingDAO extends CrudJpaDAO<CitizenRanking, CitizenRankingId> {

    @Query(nativeQuery = true,
            value = "select " +
                    "cit.cit_fiscal_code as fiscalCode, " +
                    "cit.cit_ranking as ranking, " +
                    "count(1) as totalParticipants, " +
                    "max(bcr_out.transaction_n) as maxTrxNumber, " +
                    "min(bcr_out.transaction_n) filter (where bcr_out.ranking_n = cit.cit_ranking_min) as minTrxNumber, " +
                    "cit.cit_transaction as trxNumber, " +
                    "cit.cit_award_period_id as awardPeriodId " +
                    "from bpd_citizen_ranking bcr_out " +
                    "inner join ( " +
                    "select  " +
                    "bcr.fiscal_code_c as cit_fiscal_code, " +
                    "bcr.award_period_id_n as cit_award_period_id, " +
                    "bcr.cashback_n as cit_cashback, " +
                    "bcr.transaction_n as cit_transaction, " +
                    "bcr.ranking_n as cit_ranking, " +
                    "bcr.ranking_min_n as cit_ranking_min " +
                    "from bpd_citizen_ranking bcr " +
                    "where " +
                    "bcr.fiscal_code_c = ?1 " +
                    "and bcr.award_period_id_n = ?2 " +
                    "and bcr.enabled_b = true) cit on bcr_out.award_period_id_n = cit.cit_award_period_id " +
                    "and bcr_out.enabled_b = true " +
                    "group by " +
                    "cit.cit_fiscal_code, cit.cit_ranking, cit.cit_transaction, cit.cit_award_period_id")
    Optional<CitizenTransactionConverter> getRanking(
            String fiscalCode, Long awardPeriod);

}