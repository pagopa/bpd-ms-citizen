package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestConnector;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.UnknowPSPException;
import it.gov.pagopa.bpd.citizen.connector.checkiban.exception.UnknowPSPTimeoutException;
import it.gov.pagopa.bpd.citizen.connector.jpa.*;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenEventRecord;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.GetTotalCashbackResource;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private final CitizenEventRecordDAO citizenEventRecordDAO;
    private final CitizenRankingReplicaDAO citizenRankingReplicaDAO;
    private final CheckIbanRestConnector checkIbanRestConnector;
    private final static String UNKNOWN_PSP = "UNKNOWN_PSP";

    @Autowired
    public CitizenServiceImpl(
            ObjectProvider<CitizenDAO> citizenDAO,
            ObjectProvider<CitizenRankingDAO> citizenRankingDAO,
            ObjectProvider<CitizenEventRecordDAO> citizenEventRecordDAO,
            ObjectProvider<CitizenRankingReplicaDAO> citizenRankingReplicaDAO,
            CheckIbanRestConnector checkIbanRestConnector) {
        this.citizenDAO = citizenDAO.getIfAvailable();
        this.citizenRankingDAO = citizenRankingDAO.getIfAvailable();
        this.citizenRankingReplicaDAO = citizenRankingReplicaDAO.getIfAvailable();
        this.checkIbanRestConnector = checkIbanRestConnector;
        this.citizenEventRecordDAO = citizenEventRecordDAO.getIfAvailable();
    }


    @Override
    public Citizen find(String fiscalCode) {
        return citizenDAO.findById(fiscalCode).orElseThrow(() -> new CitizenNotFoundException(fiscalCode));
    }


    @Override
    @Transactional(transactionManager = "transactionManagerPrimary")
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
    @Transactional(transactionManager = "transactionManagerPrimary")
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
            String checkResult = null;
            if (cz.getTechnicalAccountHolder() == null) {
                checkResult = checkIbanRestConnector.checkIban(cz.getPayoffInstr(), fiscalCode);
            } else {
                checkResult = "OK";
            }
            if (log.isDebugEnabled()) {
                log.debug("End CheckIbanRestClient");
            }
            if (!"KO".equals(checkResult) && checkResult != null) {
                citizen.setPayoffInstr(cz.getPayoffInstr());
                citizen.setPayoffInstrType(cz.getPayoffInstrType());
                citizen.setUpdateUser(fiscalCode);
                citizen.setUpdateDate(OffsetDateTime.now());
                citizen.setCheckInstrStatus(checkResult);
                citizen.setAccountHolderName(cz.getAccountHolderName());
                citizen.setAccountHolderSurname(cz.getAccountHolderSurname());
                citizen.setAccountHolderCF(cz.getAccountHolderCF());
                citizen.setTechnicalAccountHolder(cz.getTechnicalAccountHolder());
                citizen.setIssuerCardId(cz.getIssuerCardId());
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
            citizen.setTechnicalAccountHolder(cz.getTechnicalAccountHolder());
            citizen.setIssuerCardId(cz.getIssuerCardId());
            citizenDAO.save(citizen);
            return UNKNOWN_PSP;
        }
    }


    @SneakyThrows
    @Override
    @Transactional(transactionManager = "transactionManagerPrimary",
            propagation = Propagation.REQUIRED, rollbackFor = Exception.class,
            timeoutString = "${core.citizen.delete.transaction.timeout}"
    )
    public Boolean delete(String fiscalCode) {
        Optional<Citizen> citizenFound = citizenDAO.findById(fiscalCode);
        if (citizenFound.isPresent() && citizenFound.get().isEnabled()) {
            Citizen citizen = citizenFound.get();
            citizen.setEnabled(false);
            citizen.setAccountHolderCF(null);
            citizen.setAccountHolderName(null);
            citizen.setAccountHolderSurname(null);
            citizen.setTechnicalAccountHolder(null);
            citizen.setPayoffInstr(null);
            citizen.setPayoffInstrType(null);
            citizen.setCheckInstrStatus(null);
            citizen.setUpdateUser(fiscalCode);
            citizen.setUpdateDate(OffsetDateTime.now());
            citizen.setCancellation(OffsetDateTime.now());
            citizen.setIssuerCardId(null);
            citizenDAO.save(citizen);
            citizenRankingDAO.deactivateCitizenRankingByFiscalCode(fiscalCode);

            CitizenEventRecord citizenEventRecord = new CitizenEventRecord();
            citizenEventRecord.setFiscalCode(fiscalCode);
            citizenEventRecord.setEventTimestamp(OffsetDateTime.now());
            citizenEventRecord.setSentTimestamp(null);
            citizenEventRecord.setEventStatus(false);
            citizenEventRecordDAO.save(citizenEventRecord);

            return true;
        }
        return false;
    }

    @Override
    public GetTotalCashbackResource getTotalCashback(CitizenRankingId id) {
        Optional<GetTotalCashbackResource> citizenRanking = citizenRankingReplicaDAO.getByIdIfCitizenIsEnabled(id.getFiscalCode(), id.getAwardPeriodId());
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
                        citizenRankingReplicaDAO.getRanking(fiscalCode) :
                        citizenRankingReplicaDAO.getRanking(fiscalCode, awardPeriodId);
            } else {
                throw new CitizenNotFoundException(fiscalCode);
            }

        } else {
            throw new CitizenNotFoundException(fiscalCode);
        }

        return ranking;
    }

    @Override
    public List<CitizenTransactionMilestoneConverter> findRankingMilestoneDetails(String fiscalCode, Long awardPeriodId) {
        List<CitizenTransactionMilestoneConverter> rankingWithMilestone;

        Optional<Citizen> citizen = citizenDAO.findById(fiscalCode);
        if (citizen.isPresent()) {
            if (citizen.get().isEnabled()) {
                rankingWithMilestone = awardPeriodId == null ?
                        citizenRankingReplicaDAO.getRankingWithMilestone(fiscalCode) :
                        citizenRankingReplicaDAO.getRankingWithMilestone(fiscalCode, awardPeriodId);
            } else {
                throw new CitizenNotFoundException(fiscalCode);
            }

        } else {
            throw new CitizenNotFoundException(fiscalCode);
        }

        return rankingWithMilestone;
    }

    @Override
    public void updateEvent(CitizenEventRecord citizenEventRecord) {
        citizenEventRecordDAO.save(citizenEventRecord);
    }

    @Override
    public List<CitizenEventRecord> retrieveActiveEvents(String fiscalCode) {
        return citizenEventRecordDAO.retrieveEventToSend(fiscalCode);
    }

}
