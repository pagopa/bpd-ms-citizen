package eu.sia.bpd.demo.service;


import eu.sia.bpd.demo.integration.CitizenDAO;
import eu.sia.bpd.demo.model.Citizen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
class CitizenDAOServiceImpl implements CitizenDAOService {

    @Autowired
    private CitizenDAO citizenDAO;


    @Override
    public Citizen insert(Citizen cz) { return citizenDAO.save(cz); }

    @Override
    public Optional<Citizen> find(String fiscalCode) { return citizenDAO.findById(fiscalCode); }

    @Override
    public Citizen update(Citizen cz) { return citizenDAO.save(cz); }

    @Override
    public Citizen delete(String fiscalCode) { citizenDAO.deleteById(fiscalCode);
        return null;
    }


}
