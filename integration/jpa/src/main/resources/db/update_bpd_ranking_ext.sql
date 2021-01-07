CREATE OR REPLACE FUNCTION bpd_citizen.update_bpd_ranking_ext(bigint, numeric, int)
	RETURNS void
	LANGUAGE plpgsql
AS $function$
	BEGIN

		UPDATE bpd_citizen.bpd_ranking_ext
		SET
			max_transaction_n=subquery.maxTrxNumber,
			min_transaction_n=subquery.minTrxNumber
		FROM (
			select
				coalesce(max(transaction_n), $3) as maxTrxNumber,
				coalesce(min(transaction_n) filter (where ranking_n = $2), $3) as minTrxNumber
			from bpd_citizen.bpd_citizen_ranking bcr
			where bcr.enabled_b = true
			and bcr.award_period_id_n = $1
		) AS subquery
		WHERE award_period_id_n=$1;


		UPDATE bpd_citizen.bpd_ranking_ext
		SET
			total_participants=subquery.total_participants
		FROM (
			select count(1) as total_participants
			from bpd_citizen.bpd_citizen bc
			where enabled_b = true
		) AS subquery
		WHERE award_period_id_n=$1;

	END;
$function$
;
