package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestConnector;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.UnknowPSPException;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.UnknowPSPTimeoutException;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


/**
 * @See CitizenService
 */
@Service
@Slf4j
class CitizenServiceImpl implements CitizenService {


    private final CitizenDAO citizenDAO;
    private final CitizenRankingDAO citizenRankingDAO;
    private final CheckIbanRestConnector checkIbanRestConnector;
    private final static String UNKNOWN_PSP = "UNKNOWN_PSP";

    @Autowired
    public CitizenServiceImpl(
            CitizenDAO citizenDAO,
            CitizenRankingDAO citizenRankingDAO,
            CheckIbanRestConnector checkIbanRestConnector) {
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
                citizenFound.get().setUpdateDate(OffsetDateTime.now());
                citizenFound.get().setTimestampTC(cz.getTimestampTC());
                result = citizenDAO.save(citizenFound.get());
            }
        } else {
            cz.setFiscalCode(fiscalCode);
            cz.setInsertUser(fiscalCode);
            cz.setInsertDate(OffsetDateTime.now());
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
            if (log.isDebugEnabled()) {
                log.debug("Calling CheckIbanRestClient");
            }
            String checkResult = checkIbanRestConnector.checkIban(cz.getPayoffInstr(), fiscalCode);
            if (log.isDebugEnabled()) {
                log.debug("End CheckIbanRestClient");
            }
            if (!"KO".equals(checkResult)) {
                citizen.setPayoffInstr(cz.getPayoffInstr());
                citizen.setPayoffInstrType(cz.getPayoffInstrType());
                citizen.setUpdateUser(fiscalCode);
                citizen.setUpdateDate(OffsetDateTime.now());
                citizen.setCheckInstrStatus(checkResult);
                citizen.setAccountHolderName(cz.getAccountHolderName());
                citizen.setAccountHolderSurname(cz.getAccountHolderSurname());
                citizen.setAccountHolderCF(cz.getAccountHolderCF());
                citizenDAO.save(citizen);
            }

            return checkResult != null ? checkResult : "KO";
        } catch (UnknowPSPException | UnknowPSPTimeoutException e) {
            citizen.setPayoffInstr(cz.getPayoffInstr());
            citizen.setPayoffInstrType(cz.getPayoffInstrType());
            citizen.setUpdateUser(fiscalCode);
            citizen.setUpdateDate(OffsetDateTime.now());
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
            citizen.setAccountHolderCF(null);
            citizen.setAccountHolderName(null);
            citizen.setAccountHolderSurname(null);
            citizen.setPayoffInstr(null);
            citizen.setPayoffInstrType(null);
            citizen.setCheckInstrStatus(null);
            citizen.setUpdateUser(fiscalCode);
            citizen.setUpdateDate(OffsetDateTime.now());
            citizen.setCancellation(OffsetDateTime.now());
            citizenDAO.save(citizen);
            citizenRankingDAO.deactivateCitizenRankingByFiscalCode(fiscalCode);
        }
    }

    @Override
    public CitizenRanking getTotalCashback(CitizenRankingId id) {
        Optional<CitizenRanking> citizenRanking = citizenRankingDAO.getByIdIfCitizenIsEnabled(id.getFiscalCode(), id.getAwardPeriodId());
        if (citizenRanking != null && citizenRanking.isPresent()) {
            return citizenRanking.get();
        }

        return null;
    }

    @Override
    public List<CitizenTransactionConverter> findRankingDetails(String fiscalCode, Long awardPeriodId) {
        List<CitizenTransactionConverter> ranking;

        Optional<Citizen> citizen = citizenDAO.findById(fiscalCode);
        if (citizen.isPresent()) {
            if (citizen.get().isEnabled()) {
                ranking = awardPeriodId == null ?
                        citizenRankingDAO.getRanking(fiscalCode) :
                        citizenRankingDAO.getRanking(fiscalCode, awardPeriodId);
            } else {
                throw new CitizenNotFoundException(fiscalCode);
            }

        } else {
            throw new CitizenNotFoundException(fiscalCode);
        }

        return ranking;
    }

}
