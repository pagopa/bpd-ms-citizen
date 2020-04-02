package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.factory.ModelFactory;
import it.gov.pagopa.bpd.citizen.model.dto.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import it.gov.pagopa.bpd.citizen.model.resource.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenDAOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenDAOService citizenDAOService;
    private final CitizenResourceAssembler citizenResourceAssembler;
    private final ModelFactory<CitizenDTO, Citizen> citizenFactory;


    @Autowired
    public BpdCitizenControllerImpl(CitizenDAOService citizenDAOService,
                                    CitizenResourceAssembler citizenResourceAssembler,
                                    ModelFactory<CitizenDTO, Citizen> citizenFactory) {
        this.citizenDAOService = citizenDAOService;
        this.citizenResourceAssembler = citizenResourceAssembler;
        this.citizenFactory = citizenFactory;
    }

    @Override
    public CitizenResource create(CitizenDTO citizen) {
        System.out.println("Start create");

        final Citizen entity = citizenFactory.createModel(citizen);
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

        final Citizen entity = citizenFactory.createModel(citizen);
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
