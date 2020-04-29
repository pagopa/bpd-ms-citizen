package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.dao.CitizenDAO;
import it.gov.pagopa.bpd.citizen.dao.CitizenRankingDAO;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.citizen.dao.model.CitizenRanking;
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
        cz.setUpdateUser(fiscalCode);
        return citizenDAO.save(cz);
    }


    @Override
    public Citizen patch(String fiscalCode, Citizen cz) {
        Citizen citizen = citizenDAO.findById(fiscalCode)
                .orElseThrow(() -> new CitizenNotFoundException(fiscalCode));
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
        return citizenDAO.count();
    }

    @Override
    public CitizenRanking findRanking(String fiscalCode, Long awardPeriodId) {
        CitizenRanking ranking = citizenRankingDAO.findByFiscalCodeAndAwardPeriodId(fiscalCode, awardPeriodId);
        if (ranking == null) {
            throw new CitizenRankingNotFoundException(fiscalCode);
        }
        return ranking;
    }
}
