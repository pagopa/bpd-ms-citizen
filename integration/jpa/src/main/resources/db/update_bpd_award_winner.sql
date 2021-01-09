create or replace function bpd_citizen.update_bpd_award_winner()
 RETURNS void
 LANGUAGE plpgsql
as $function$


begin

	/**
	 * this table contains the award period closest to closing
	 */
	CREATE TEMPORARY TABLE ending_award_period (
		actual_end DATE
		,award_period_id bigserial
		) on commit drop;

	INSERT INTO ending_award_period (
		actual_end
		,award_period_id
		)
	SELECT ap.aw_period_end_d + ap.aw_grace_period_n AS actual_end
		,ap.award_period_id_n AS award_period_id
	FROM public.dblink('bpd_award_period_remote','SELECT award_period_id_n, aw_period_start_d, aw_period_end_d, aw_grace_period_n FROM bpd_award_period.bpd_award_period where CURRENT_DATE between aw_period_start_d and aw_period_end_d + aw_grace_period_n + 1') AS ap(award_period_id_n bigint, aw_period_start_d date, aw_period_end_d date, aw_grace_period_n smallint)
	ORDER BY actual_end limit 1;

	/**
	 * if an award period ends today then a new winner is defined
	 * and a new record is added to the bpd_award_winner table
	 */
	IF (
			CURRENT_DATE = (
				SELECT ap.aw_period_end_d + ap.aw_grace_period_n + 1 AS actual_end
					FROM public.dblink('bpd_award_period_remote','SELECT award_period_id_n, aw_period_start_d, aw_period_end_d, aw_grace_period_n FROM bpd_award_period.bpd_award_period WHERE CURRENT_DATE between aw_period_start_d and aw_period_end_d + aw_grace_period_n + 1') AS ap(award_period_id_n bigint, aw_period_start_d date, aw_period_end_d date, aw_grace_period_n smallint)
					ORDER BY actual_end limit 1
				)
			) THEN PERFORM bpd_citizen.update_bpd_award_winner((
				SELECT award_period_id
				FROM ending_award_period
				));
	END IF ;

END;$function$
;
