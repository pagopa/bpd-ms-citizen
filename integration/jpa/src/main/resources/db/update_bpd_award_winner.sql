CREATE OR REPLACE FUNCTION bpd_citizen.update_bpd_award_winner(award_period_id bigint)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

BEGIN

	insert into bpd_citizen.bpd_award_winner(
		award_period_id_n,
		fiscal_code_s,
		payoff_instr_s,
		amount_n,
		insert_date_t,
		insert_user_s,
		enabled_b,
		aw_period_start_d,
		aw_period_end_d,
		jackpot_n,
		cashback_n,
		typology_s,
		account_holder_cf_s,
		account_holder_name_s,
		account_holder_surname_s,
		check_instr_status_s,
		account_holder_s
	)
	select
		bcr.award_period_id_n,
		bcr.fiscal_code_c as fiscal_code,
		bc.payoff_instr_s as payoff_instr,
		CASE WHEN bcr.ranking_n <= bcr.ranking_min_n THEN
			(CASE when bcr.cashback_n <= bcr.max_cashback_n then bcr.cashback_n + bap.amount_max_n
			else bcr.max_cashback_n + bap.amount_max_n END)
			ELSE (CASE when bcr.cashback_n <= bcr.max_cashback_n then bcr.cashback_n else bcr.max_cashback_n END) END as amount,
		CURRENT_TIMESTAMP,
		'update_bpd_award_winner' AS insert_user_s,
		true,
		bap.aw_period_start_d,
		bap.aw_period_end_d,
		CASE WHEN bcr.ranking_n <= bcr.ranking_min_n THEN bap.amount_max_n ELSE 0 END as jackpot,
		CASE when bcr.cashback_n <= bcr.max_cashback_n then bcr.cashback_n else bcr.max_cashback_n END as cashback,
		CASE
			WHEN bcr.cashback_n <= 0 THEN '02'
			WHEN bcr.ranking_n <= bcr.ranking_min_n and  bcr.cashback_n > 0 THEN '03' ELSE '01' END as typology,
		bc.account_holder_cf_s,
		bc.account_holder_name_s,
		bc.account_holder_surname_s,
		bc.check_instr_status_s,
		bc.technical_account_holder_s
	from bpd_citizen.bpd_citizen_ranking bcr
		inner join bpd_citizen.bpd_citizen bc on bcr.fiscal_code_c = bc.fiscal_code_s
		inner join bpd_award_period.bpd_award_period bap on bcr.award_period_id_n = bap.award_period_id_n
	where bcr.award_period_id_n = award_period_id
		and bcr.transaction_n >= bap.trx_volume_min_n
		and bc.enabled_b = true;




END;$function$
;
