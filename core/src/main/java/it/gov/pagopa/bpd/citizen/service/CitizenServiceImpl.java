package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.dao.CitizenDAO;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
class CitizenServiceImpl implements CitizenService {


    private final CitizenDAO citizenDAO;


    @Autowired
    public CitizenServiceImpl(CitizenDAO citizenDAO) {
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
    public Citizen patch(String fiscalCode, Citizen cz) {
        Citizen citizen = citizenDAO.getOne(fiscalCode);
        citizen.setPayoffInstr(cz.getPayoffInstr());
        citizen.setPayoffInstrType(cz.getPayoffInstrType());
        citizen.setUpdateUser(fiscalCode);
        return citizenDAO.save(citizen);
    }


    @Override
    public void delete(String fiscalCode) {
        Citizen citizen = citizenDAO.getOne(fiscalCode);
        citizen.setEnabled(false);
        citizen.setUpdateUser(fiscalCode);
        citizenDAO.save(citizen);
    }

}