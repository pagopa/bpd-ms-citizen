create or replace function bpd_citizen.update_bpd_award_winner(award_period_id bigint)
 RETURNS void
 LANGUAGE plpgsql
as $function$


begin

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
		technical_account_holder_s
	)
	select
		bcr.award_period_id_n,
		bcr.fiscal_code_c as fiscal_code,
		bc.payoff_instr_s as payoff_instr,
		case
			when coalesce(bcr.ranking_n,bre.total_participants+1) <= coalesce(bcr.ranking_min_n,bap.ranking_min_n) then
				(
				case
					when coalesce(bcr.cashback_n,0) <= coalesce(bcr.max_cashback_n,bap.period_cashback_max_n) then bcr.cashback_n + bap.amount_max_n
					else bcr.max_cashback_n + bap.amount_max_n
				end
				)
			else (
				case
					when coalesce(bcr.cashback_n,0) <= coalesce(bcr.max_cashback_n,bap.period_cashback_max_n) then bcr.cashback_n
					else bcr.max_cashback_n end
			)
		end as amount,
		current_timestamp,
		'update_bpd_award_winner' as insert_user_s,
		true,
		bap.aw_period_start_d,
		bap.aw_period_end_d,
		case when bcr.ranking_n <= bcr.ranking_min_n then bap.amount_max_n else 0 end as jackpot,
		case when bcr.cashback_n <= bcr.max_cashback_n then bcr.cashback_n else bcr.max_cashback_n end as cashback,
		case
			when bcr.cashback_n <= 0 then '02'
			when bcr.ranking_n <= bcr.ranking_min_n and  bcr.cashback_n > 0 then '03' else '01' end as typology,
		bc.account_holder_cf_s,
		bc.account_holder_name_s,
		bc.account_holder_surname_s,
		bc.check_instr_status_s,
		bc.technical_account_holder_s
	from bpd_citizen.bpd_citizen_ranking bcr
		inner join bpd_citizen.bpd_citizen bc on bcr.fiscal_code_c = bc.fiscal_code_s
		inner join bpd_award_period.bpd_award_period bap on bcr.award_period_id_n = bap.award_period_id_n
		inner join bpd_citizen.bpd_ranking_ext bre on bcr.award_period_id_n = bre.award_period_id_n
	where bcr.award_period_id_n = 0
		and bcr.transaction_n >= bap.trx_volume_min_n
		and bc.enabled_b = true
	ON conflict(fiscal_code_s, award_period_id_n) DO nothing;

END;$function$
;
