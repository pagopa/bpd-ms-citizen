package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.citizen.dao.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.factory.ModelFactory;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenService citizenService;
    private final CitizenResourceAssembler citizenResourceAssembler;
    private final ModelFactory<CitizenDTO, Citizen> citizenFactory;
    private final ModelFactory<CitizenPatchDTO, Citizen> citizenPatchFactory;
    private final CitizenRankingResourceAssembler citizenRankingResourceAssembler;


    @Autowired
    public BpdCitizenControllerImpl(CitizenService citizenService,
                                    CitizenResourceAssembler citizenResourceAssembler,
                                    ModelFactory<CitizenDTO, Citizen> citizenFactory,
                                    ModelFactory<CitizenPatchDTO, Citizen> citizenPatchFactory, CitizenRankingResourceAssembler citizenRankingResourceAssembler) {
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

        final Optional<Citizen> citizen = citizenService.find(fiscalCode);
        return citizenResourceAssembler.toResource(citizen.get());
    }

    @Override
    public CitizenResource update(String fiscalCode, CitizenDTO citizen) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.update");
            logger.debug("fiscalCode = [" + fiscalCode + "], citizen = [" + citizen + "]");
        }

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);
        Citizen citizenEntity = citizenService.update(fiscalCode, entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }

    @Override
    public CitizenResource updatePaymentMethod(String fiscalCode, CitizenPatchDTO citizen) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.updatePaymentMethod");
            logger.debug("fiscalCode = [" + fiscalCode + "], citizen = [" + citizen + "]");
        }

        try {
            final Citizen entity = citizenPatchFactory.createModel(citizen);
            entity.setPayoffInstr(citizen.getPayoffInstr());
            entity.setPayoffInstrType(citizen.getPayoffInstrType());
            Citizen citizenEntity = citizenService.patch(fiscalCode, entity);
            return citizenResourceAssembler.toResource(citizenEntity);
        } catch (EntityNotFoundException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void updateTC(String fiscalCode, CitizenDTO citizen) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.updateTC");
            logger.debug("fiscalCode = [" + fiscalCode + "], citizen = [" + citizen + "]");
        }

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);

        Citizen citizenEntity = citizenService.update(fiscalCode, entity);
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
    public CitizenRankingResource findRanking(@Valid @NotBlank String fiscalCode, Long awardPeriodId) {
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

