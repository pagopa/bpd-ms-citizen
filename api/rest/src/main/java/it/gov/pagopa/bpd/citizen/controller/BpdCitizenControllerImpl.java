package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.factory.ModelFactory;
import it.gov.pagopa.bpd.citizen.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
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
    public CitizenResource find(String fiscalCode) {
        logger.debug("Start find by fiscal code");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Optional<Citizen> citizen = citizenDAOService.find(fiscalCode);
        return citizenResourceAssembler.toResource(citizen.get());
    }

    @Override
    public CitizenResource update(String fiscalCode, CitizenDTO citizen) {
        logger.debug("Start update");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);
        Citizen citizenEntity = citizenDAOService.update(fiscalCode, entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }

    @Override
    public CitizenResource updatePaymentMethod(String fiscalCode, CitizenDTO citizen) {
        logger.debug("Start update");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);
        entity.setPayoffInstr(citizen.getPayoffInstr());
        entity.setPayoffInstrType(citizen.getPayoffInstrType());
        Citizen citizenEntity = citizenDAOService.patch(fiscalCode, entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }


    @Override
    public void delete(String fiscalCode) {
        logger.debug("Start delete");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        citizenDAOService.delete(fiscalCode);
    }

    @Override
    public void updateTC(String fiscalCode, CitizenDTO citizen) {
        logger.debug("Start update T&C");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);

        Citizen citizenEntity = citizenDAOService.update(fiscalCode, entity);
    }
}
