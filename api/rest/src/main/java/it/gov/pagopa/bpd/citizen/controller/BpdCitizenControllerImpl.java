package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.command.CitizenDAOService;
import it.gov.pagopa.bpd.citizen.model.dto.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import it.gov.pagopa.bpd.citizen.model.resource.CitizenResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenDAOService citizenDAOService;
    private final CitizenResourceAssembler citizenResourceAssembler;


    @Autowired
    public BpdCitizenControllerImpl(CitizenDAOService citizenDAOService, CitizenResourceAssembler citizenResourceAssembler) {
        this.citizenDAOService = citizenDAOService;
        this.citizenResourceAssembler = citizenResourceAssembler;
    }

    @Override
    public CitizenResource insert(CitizenDTO citizen) {
        System.out.println("Start insert");

        final Citizen entity = citizen.toEntity();
        Citizen citizenEntity = citizenDAOService.insert(entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }

    @Override
    public CitizenResource find(String fiscalCode) {
        System.out.println("Start find by fiscal code");
        System.out.println("fiscalCode = [" + fiscalCode + "]");

        final Optional<Citizen> citizen = citizenDAOService.find(fiscalCode);

        return citizenResourceAssembler.toResource(citizen.get());
    }

    public CitizenResource update(CitizenDTO citizen) {
        System.out.println("Start update");

        final Citizen entity = citizen.toEntity();
        Citizen citizenEntity = citizenDAOService.update(entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }

    @Override
    public void delete(String fiscalCode) {
        System.out.println("Start delete");
        System.out.println("fiscalCode = [" + fiscalCode + "]");

        citizenDAOService.delete(fiscalCode);

    }

}
