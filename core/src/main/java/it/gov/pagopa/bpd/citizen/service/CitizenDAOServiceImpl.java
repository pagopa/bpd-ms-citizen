package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.service.model.entity.Citizen;
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
    public Optional<Citizen> find(String fiscalCode) {
        return citizenDAO.findById(fiscalCode);
    }

    @Override
    public Citizen update(String fiscalCode, Citizen cz) {
        cz.setUpdateUser(fiscalCode);
        return citizenDAO.save(cz);
    }

    @Override
    public void delete(String fiscalCode) {
        Citizen citizen = citizenDAO.getOne(fiscalCode);
        update(fiscalCode, citizen);
    }
}
