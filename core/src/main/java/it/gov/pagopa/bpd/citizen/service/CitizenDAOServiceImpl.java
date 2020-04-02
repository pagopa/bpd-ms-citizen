package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.CitizenDAO;
import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
class CitizenDAOServiceImpl implements CitizenDAOService {


    private final CitizenDAO citizenDAO;

    @Autowired
    public CitizenDAOServiceImpl(CitizenDAO citizenDAO) {
        this.citizenDAO = citizenDAO;
    }

    @Override
    public Citizen insert(Citizen cz) {
        if (citizenDAO.existsById(cz.getFiscalCode())) {
            throw new RuntimeException("Già esistente");
        }
        cz.setInsertUser(cz.getFiscalCode());
        return citizenDAO.save(cz);
    }

    @Override
    public Optional<Citizen> find(String fiscalCode) {
        return citizenDAO.findById(fiscalCode);
    }

    @Override
    public Citizen update(Citizen cz) {
        cz.setUpdateUser(cz.getFiscalCode());
        return citizenDAO.save(cz);
    }

    @Override
    public void delete(String fiscalCode) {
        citizenDAO.deleteById(fiscalCode);
    }
}
