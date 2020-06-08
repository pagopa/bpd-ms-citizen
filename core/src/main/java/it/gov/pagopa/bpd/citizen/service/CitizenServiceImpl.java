package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotEnabledException;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotFoundException;
import it.gov.pagopa.bpd.citizen.exception.CitizenRankingNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * @See CitizenService
 */
@Service
@Slf4j
class CitizenServiceImpl implements CitizenService {


    private final CitizenDAO citizenDAO;
    private final CitizenRankingDAO citizenRankingDAO;


    @Autowired
    public CitizenServiceImpl(CitizenDAO citizenDAO, CitizenRankingDAO citizenRankingDAO) {
        this.citizenDAO = citizenDAO;
        this.citizenRankingDAO = citizenRankingDAO;
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
    public Citizen patch(String fiscalCode, Citizen cz) {
        Citizen citizen = citizenDAO.findById(fiscalCode)
                .orElseThrow(() -> new CitizenNotFoundException(fiscalCode));
        if (!citizen.isEnabled()) {
            throw new CitizenNotEnabledException(fiscalCode);
        }
        citizen.setPayoffInstr(cz.getPayoffInstr());
        citizen.setPayoffInstrType(cz.getPayoffInstrType());
        citizen.setUpdateUser(fiscalCode);
        return citizenDAO.save(citizen);
    }


    @Override
    public void delete(String fiscalCode) {
        Optional<Citizen> citizen = citizenDAO.findById(fiscalCode);
        if (citizen.isPresent()) {
            citizen.get().setEnabled(false);
            citizen.get().setUpdateUser(fiscalCode);
            citizenDAO.save(citizen.get());
        }
    }

    @Override
    public Long calculateAttendeesNumber() {
        return citizenDAO.count((Example<Citizen>) null);
    }

    @Override
    public CitizenRanking findRanking(String fiscalCode, Long awardPeriodId) {

        Optional<Citizen> citizen = citizenDAO.findById(fiscalCode);

        if (!citizen.isPresent()) {
            throw new CitizenNotFoundException(fiscalCode);
        }

        if (!citizen.get().isEnabled()) {
            throw new CitizenNotEnabledException(fiscalCode);
        }

        Optional<CitizenRanking> ranking = citizenRankingDAO.findById(
                new CitizenRankingId(fiscalCode, awardPeriodId));

        if (!ranking.isPresent()) {
            throw new CitizenRankingNotFoundException(fiscalCode);
        }

        return ranking.get();
    }
}
