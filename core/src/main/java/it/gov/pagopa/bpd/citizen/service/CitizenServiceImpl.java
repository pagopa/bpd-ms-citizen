package it.gov.pagopa.bpd.citizen.service;

import feign.FeignException;
import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestConnector;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.UnknowPSPException;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenTransaction;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotFoundException;
import it.gov.pagopa.bpd.citizen.exception.CitizenRankingNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @See CitizenService
 */
@Service
@Slf4j
class CitizenServiceImpl implements CitizenService {


    private final CitizenTransactionDAO citizenTransactionDAO;
    private final CitizenDAO citizenDAO;
    private final CitizenRankingDAO citizenRankingDAO;
    private final CheckIbanRestConnector checkIbanRestConnector;
    private final static String UNKNOWN_PSP = "UNKNOWN_PSP";

    @Autowired
    public CitizenServiceImpl(
            CitizenTransactionDAO citizenTransactionDAO,
            CitizenDAO citizenDAO,
            CitizenRankingDAO citizenRankingDAO,
            CheckIbanRestConnector checkIbanRestConnector) {
        this.citizenTransactionDAO = citizenTransactionDAO;
        this.citizenDAO = citizenDAO;
        this.citizenRankingDAO = citizenRankingDAO;
        this.checkIbanRestConnector = checkIbanRestConnector;
    }


    @Override
    public Citizen find(String fiscalCode) {
        return citizenDAO.findById(fiscalCode).orElseThrow(() -> new CitizenNotFoundException(fiscalCode));
    }


    @Override
    public Citizen update(String fiscalCode, Citizen cz) {
        final Citizen result;
        final Optional<Citizen> citizenFound = citizenDAO.findById(fiscalCode);
        if (citizenFound.isPresent()) {
            if (citizenFound.get().isEnabled()) {
                result = citizenFound.get();
            } else {
                citizenFound.get().setEnabled(true);
                citizenFound.get().setUpdateUser(fiscalCode);
                citizenFound.get().setTimestampTC(cz.getTimestampTC());
                result = citizenDAO.save(citizenFound.get());
            }
        } else {
            cz.setFiscalCode(fiscalCode);
            cz.setUpdateUser(fiscalCode);
            result = citizenDAO.save(cz);
        }
        return result;
    }


    @Override
    public String patch(String fiscalCode, Citizen cz) {

        Citizen citizen = citizenDAO.findById(fiscalCode)
                .orElseThrow(() -> new CitizenNotFoundException(fiscalCode));
        if (!citizen.isEnabled()) {
            throw new CitizenNotEnabledException(fiscalCode);
        }

        try {
            String checkResult = checkIbanRestConnector.checkIban(cz.getPayoffInstr(), fiscalCode);
            if (!checkResult.equals("KO")) {
                citizen.setPayoffInstr(cz.getPayoffInstr());
                citizen.setPayoffInstrType(cz.getPayoffInstrType());
                citizen.setUpdateUser(fiscalCode);
                citizen.setCheckInstrStatus(checkResult);
                citizen.setAccountHolderName(cz.getAccountHolderName());
                citizen.setAccountHolderSurname(cz.getAccountHolderSurname());
                citizen.setAccountHolderCF(cz.getAccountHolderCF());
                citizenDAO.save(citizen);
            }
            return checkResult;
        } catch (UnknowPSPException e) {
            citizen.setPayoffInstr(cz.getPayoffInstr());
            citizen.setPayoffInstrType(cz.getPayoffInstrType());
            citizen.setUpdateUser(fiscalCode);
            citizen.setCheckInstrStatus(UNKNOWN_PSP);
            citizen.setAccountHolderName(cz.getAccountHolderName());
            citizen.setAccountHolderSurname(cz.getAccountHolderSurname());
            citizen.setAccountHolderCF(cz.getAccountHolderCF());
            citizenDAO.save(citizen);
            return UNKNOWN_PSP;
        }
    }


    @Override
    public void delete(String fiscalCode) {
        Optional<Citizen> citizenFound = citizenDAO.findById(fiscalCode);
        if (citizenFound.isPresent() && citizenFound.get().isEnabled()) {
            Citizen citizen = citizenFound.get();
            citizen.setEnabled(false);
            citizen.setUpdateUser(fiscalCode);
            citizenDAO.save(citizen);
        }
    }

    @Override
    public Long calculateAttendeesNumber() {
        return citizenDAO.count();
    }


    @Override
    public CitizenTransaction getTransactionDetails(String fiscalCode, Long awardPeriodId) {
        return citizenTransactionDAO.getTransactionDetails(fiscalCode, awardPeriodId);
    }


    @Override
    public CitizenRanking findRanking(String fiscalCode, Long awardPeriodId) {

        Optional<CitizenRanking> ranking = citizenRankingDAO.findById(
                new CitizenRankingId(fiscalCode, awardPeriodId));

        if (!ranking.isPresent()) {
            throw new CitizenRankingNotFoundException(fiscalCode);
        }

        return ranking.get();
    }
}
