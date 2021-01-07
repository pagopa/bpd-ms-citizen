CREATE OR REPLACE FUNCTION bpd_citizen.update_bpd_citizen_ranking()
 RETURNS TABLE(fiscal_code_out character varying, payoff_instr_out character varying, aw_period_out bigint, ranking_out numeric, amount_out numeric, aw_period_start_out date, aw_period_end_out date)
 LANGUAGE plpgsql
AS $function$
declare
		aw_period record;
	begin

	insert into bpd_citizen.bpd_elab_transaction_temp(
		id_trx_acquirer_s,
		trx_timestamp_t,
		acquirer_c,
		acquirer_id_s,
		operation_type_c,
		award_period_id_n,
		hpan_s,
		score_n,
		amount_i,
		insert_date_t,
		update_date_t,
		fiscal_code_s,
		correlation_id_s
	)
	select wt.id_trx_acquirer_s,
			wt.trx_timestamp_t,
			wt.acquirer_c,
			wt.acquirer_id_s,
			wt.operation_type_c,
			wt.award_period_id_n,
			wt.hpan_s,
			wt.score_n,
			wt.amount_i,
			wt.insert_date_t,
			wt.update_date_t,
			wt.fiscal_code_s,
			wt.correlation_id_s
		from bpd_citizen.bpd_citizen bc
			inner join public.dblink('bpd_winning_transaction_remote',
				'select id_trx_acquirer_s,trx_timestamp_t,acquirer_c,acquirer_id_s,operation_type_c,award_period_id_n, hpan_s, score_n, amount_i, insert_date_t, update_date_t, fiscal_code_s, correlation_id_s from bpd_winning_transaction.bpd_winning_transaction master where master.enabled_b = true and master.elab_ranking_b = false
						and (master.operation_type_c!=''01'' or (master.operation_type_c=''01'' and (
							((nullif(master.correlation_id_s,'''') is not null)
								and exists (select * from bpd_winning_transaction.bpd_winning_transaction bin where bin.operation_type_c!=''01'' and master.hpan_s=bin.hpan_s and master.correlation_id_s=bin.correlation_id_s and master.acquirer_c=bin.acquirer_c and master.acquirer_id_s=bin.acquirer_id_s and bin.enabled_b=true and master.award_period_id_n=bin.award_period_id_n))
						or
							((nullif(master.correlation_id_s,'''') is null)
								and exists (select * from bpd_winning_transaction.bpd_winning_transaction bin  where bin.operation_type_c!=''01'' and master.hpan_s = bin.hpan_s and master.amount_i =bin.amount_i and master.merchant_id_s =bin.merchant_id_s and master.terminal_id_s =bin.terminal_id_s and master.acquirer_c=bin.acquirer_c and master.acquirer_id_s=bin.acquirer_id_s and bin.enabled_b=true and master.award_period_id_n=bin.award_period_id_n))
							)
						)
					)') AS wt(id_trx_acquirer_s varchar,trx_timestamp_t timestamptz,acquirer_c varchar, acquirer_id_s varchar, operation_type_c varchar,award_period_id_n bigint, hpan_s varchar, score_n numeric, amount_i numeric, insert_date_t timestamptz, update_date_t timestamptz, fiscal_code_s varchar, correlation_id_s varchar) on wt.fiscal_code_s=bc.fiscal_code_s
			where bc.enabled_b =true;

	/**
	 * trx_daily calculate the cashback accumulated by citizens
	 * grouped by fiscal_code and award period
	 */
	WITH trx_daily as (
	   SELECT
			bc.fiscal_code_s as fiscal_code
			,sum(wt.score_n) AS cashback
			,wt.award_period_id_n as award_period
			,max(wt.insert_date_t) as max_trx_insert
			,count(1) filter(where wt.score_n > 0) as trx_acquisto
			,count(1) filter(where wt.score_n <= 0  and (nullif(correlation_id_s,'''') is null or (select count(1) as conteggio from bpd_winning_transaction.bpd_winning_transaction tmp where tmp.operation_type_c!='01' and tmp.correlation_id_s=wt.correlation_id_s and tmp.acquirer_id_s =wt.acquirer_id_s and tmp.amount_i=wt.amount_i and tmp.hpan_s=wt.hpan_s and tmp.award_period_id_n=wt.award_period_id_n)>0)) as storni
		FROM bpd_citizen.bpd_citizen bc
		INNER JOIN bpd_citizen.bpd_elab_transaction_temp wt ON wt.fiscal_code_s = bc.fiscal_code_s
		INNER JOIN public.dblink('bpd_award_period_remote','SELECT award_period_id_n, aw_period_start_d, aw_period_end_d, aw_grace_period_n, ranking_min_n FROM bpd_award_period.bpd_award_period') AS ap(award_period_id_n bigint, aw_period_start_d date, aw_period_end_d date, aw_grace_period_n smallint, ranking_min_n numeric) ON ap.award_period_id_n = wt.award_period_id_n
		where bc.enabled_b = true
		GROUP BY bc.fiscal_code_s, wt.award_period_id_n, ap.ranking_min_n
    )


	/**
	 * the bpd_citizen_ranking table is updated with the new calculated scores
	 */
	INSERT INTO bpd_citizen.bpd_citizen_ranking (
		fiscal_code_c
		,transaction_n
		,cashback_n
		,award_period_id_n
		,insert_date_t
		,insert_user_s
		,ranking_date_t
		)
	SELECT trx_dly.fiscal_code
		,trx_dly.trx_acquisto - trx_dly.storni as transazioni
		,trx_dly.cashback AS cashback
		,trx_dly.award_period
		,CURRENT_TIMESTAMP AS insert_date_t
		,'update_bpd_citizen_ranking' AS insert_user_s
		,trx_dly.max_trx_insert as ranking_date_t
	FROM trx_daily trx_dly ON conflict(fiscal_code_c, award_period_id_n) DO
	UPDATE
	SET cashback_n = bpd_citizen.bpd_citizen_ranking.cashback_n + excluded.cashback_n
		,transaction_n = bpd_citizen.bpd_citizen_ranking.transaction_n + excluded.transaction_n
		,update_date_t = CURRENT_TIMESTAMP
		,update_user_s = 'update_bpd_citizen_ranking'
		,ranking_date_t= excluded.ranking_date_t
		,enabled_b= true;

	for aw_period in
	   	SELECT *
		FROM public.dblink('bpd_award_period_remote','SELECT award_period_id_n, aw_period_start_d, aw_period_end_d, aw_grace_period_n, ranking_min_n FROM bpd_award_period.bpd_award_period WHERE CURRENT_DATE between aw_period_start_d and aw_period_end_d + aw_grace_period_n') AS ap(award_period_id_n bigint, aw_period_start_d date, aw_period_end_d date, aw_grace_period_n smallint, ranking_min_n numeric)
	loop
		UPDATE  bpd_citizen.bpd_citizen_ranking cr
		SET    ranking_n = sub.rn,
				ranking_min_n = sub.ranking_min_n,
				max_cashback_n = sub.period_cashback_max_n
		FROM  (SELECT tmp.fiscal_code_c, tmp.award_period_id_n, tmp.transaction_n, tmp.ranking_min_n, tmp.period_cashback_max_n, row_number() over(order by 1) AS rn
	from (
		SELECT bcr.fiscal_code_c, bcr.award_period_id_n, bcr.transaction_n, ap.ranking_min_n, ap.period_cashback_max_n
				FROM bpd_citizen.bpd_citizen_ranking bcr
				inner join bpd_citizen.bpd_citizen bc on bcr.fiscal_code_c = bc.fiscal_code_s
				inner join public.dblink('bpd_award_period_remote',format('SELECT award_period_id_n, ranking_min_n, period_cashback_max_n FROM bpd_award_period.bpd_award_period WHERE award_period_id_n= %L',aw_period.award_period_id_n)) AS ap(award_period_id_n bigint, ranking_min_n numeric, period_cashback_max_n numeric) on bcr.award_period_id_n = ap.award_period_id_n
				WHERE bcr.award_period_id_n = aw_period.award_period_id_n
				and bc.enabled_b = true
				ORDER BY transaction_n desc, fiscal_code_c asc) as tmp) sub
		WHERE sub.fiscal_code_c = cr.fiscal_code_c
		and sub.award_period_id_n = cr.award_period_id_n;

		/*
		 * update bpd_ranking_ext
		 */
		PERFORM bpd_citizen.update_bpd_ranking_ext(aw_period.award_period_id_n, aw_period.ranking_min_n);
	end loop;

	/*
	 * update winning transaction
	 */
	update bpd_winning_transaction.bpd_winning_transaction wt
		set elab_ranking_b = true
	from (select id_trx_acquirer_s, trx_timestamp_t, acquirer_c,acquirer_id_s,operation_type_c
			from bpd_citizen.bpd_elab_transaction_temp) elab
	where wt.id_trx_acquirer_s=elab.id_trx_acquirer_s
		and wt.trx_timestamp_t=elab.trx_timestamp_t
		and wt.acquirer_c=elab.acquirer_c
		and wt.acquirer_id_s=elab.acquirer_id_s
		and wt.operation_type_c=elab.operation_type_c;

	truncate table bpd_citizen.bpd_elab_transaction_temp;

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

END;
$function$
;
