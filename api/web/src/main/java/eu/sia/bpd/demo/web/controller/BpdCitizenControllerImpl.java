package eu.sia.bpd.demo.web.controller;

import eu.sia.bpd.demo.service.CitizenDAOService;
import eu.sia.bpd.demo.web.controller.model.dto.CitizenDTO;
import eu.sia.bpd.demo.model.Citizen;
import eu.sia.meda.core.controller.StatelessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenDAOService citizenDAOService;


    @Autowired
    public BpdCitizenControllerImpl(CitizenDAOService citizenDAOService) {
        this.citizenDAOService = citizenDAOService;
    }

    @Override
    public Citizen insert(CitizenDTO citizen) {
        System.out.println("Start insert");

        final Citizen entity = citizen.toEntity();
        return citizenDAOService.insert(entity);

    }

    @Override
    public Optional<Citizen> find(String fiscalCode) {
        System.out.println("Start find by fiscal code");
        System.out.println("fiscalCode = [" + fiscalCode + "]");

        final Optional<Citizen> citizen = citizenDAOService.find(fiscalCode);

        return citizen;
    }

    public Citizen update(CitizenDTO citizen) {
        System.out.println("Start update");

        final Citizen entity = citizen.toEntity();
        return citizenDAOService.update(entity);
    }

    @Override
    public void delete(String fiscalCode) {
        System.out.println("Start delete");
        System.out.println("fiscalCode = [" + fiscalCode + "]");

        final Citizen citizen = citizenDAOService.delete(fiscalCode);

    }

}



