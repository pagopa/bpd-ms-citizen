package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.factory.CitizenPatchFactory;
import it.gov.pagopa.bpd.citizen.factory.ModelFactory;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchResource;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see BpdCitizenController
 */
@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenService citizenService;
    private final CitizenResourceAssembler citizenResourceAssembler;
    private final ModelFactory<CitizenDTO, Citizen> citizenFactory;
    private final CitizenPatchFactory citizenPatchFactory;
    private final CitizenRankingResourceAssembler citizenRankingResourceAssembler;


    @Autowired
    public BpdCitizenControllerImpl(CitizenService citizenService,
                                    CitizenResourceAssembler citizenResourceAssembler,
                                    ModelFactory<CitizenDTO, Citizen> citizenFactory,
                                    CitizenPatchFactory citizenPatchFactory,
                                    CitizenRankingResourceAssembler citizenRankingResourceAssembler) {
        this.citizenService = citizenService;
        this.citizenResourceAssembler = citizenResourceAssembler;
        this.citizenFactory = citizenFactory;
        this.citizenPatchFactory = citizenPatchFactory;
        this.citizenRankingResourceAssembler = citizenRankingResourceAssembler;
    }

    @Override
    public CitizenResource find(String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.find");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
        }

        final Citizen citizen = citizenService.find(fiscalCode);
        return citizenResourceAssembler.toResource(citizen);
    }

    @Override
    public CitizenResource update(String fiscalCode, CitizenDTO citizen) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.update");
            logger.debug("fiscalCode = [" + fiscalCode + "], citizen = [" + citizen + "]");
        }

        final Citizen entity = citizenFactory.createModel(citizen);
        Citizen citizenEntity = citizenService.update(fiscalCode, entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }

    @Override
    public CitizenPatchResource updatePaymentMethod(String fiscalCode, CitizenPatchDTO citizen) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.updatePaymentMethod");
            logger.debug("fiscalCode = [" + fiscalCode + "], citizen = [" + citizen + "]");
        }

        CitizenPatchResource response = new CitizenPatchResource();

        final Citizen entity = citizenPatchFactory.createModel(citizen);

        response.setValidationStatus(citizenService.patch(fiscalCode, entity));

        return response;
    }

    @Override
    public void delete(String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.delete");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
        }
        citizenService.delete(fiscalCode);
    }

    @Override
    public CitizenRankingResource findRanking(String fiscalCode, Long awardPeriodId) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.findRanking");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
            logger.debug("awardPeriodId = [" + awardPeriodId + "]");
        }


        CitizenRanking foundRanking = citizenService.findRanking(fiscalCode, awardPeriodId);
        Long attendeesNumber = citizenService.calculateAttendeesNumber();

        return citizenRankingResourceAssembler.toResource(foundRanking, attendeesNumber);
    }

}

