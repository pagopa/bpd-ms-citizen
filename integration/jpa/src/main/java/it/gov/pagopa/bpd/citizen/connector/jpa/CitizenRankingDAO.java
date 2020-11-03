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
                    "ranking_n as ranking" +
                    ",count(1) as totalParticipants" +
                    ",(select transaction_n from bpd_citizen_ranking where ranking_n = 1) as maxTrxNumber " +
                    ",(select transaction_n from bpd_citizen_ranking where ranking_n = ranking_min_n) as minTrxNumber " +
                    ",transaction_n as trxNumber " +
                    ",award_period_id_n as awardPeriodId " +
                    "from bpd_citizen_ranking " +
                    "where fiscal_code_c = ?1 " +
                    "and award_period_id_n = ?2 " +
                    "group by ranking_n, transaction_n, award_period_id_n")
    Optional<CitizenTransactionConverter> getRanking(
            String fiscalCode, Long awardPeriod);

}