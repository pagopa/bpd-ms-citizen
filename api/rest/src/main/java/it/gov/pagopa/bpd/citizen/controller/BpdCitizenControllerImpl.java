package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenCashbackResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingMilestoneResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionMilestoneConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.factory.CitizenPatchFactory;
import it.gov.pagopa.bpd.citizen.factory.ModelFactory;
import it.gov.pagopa.bpd.citizen.model.*;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * @see BpdCitizenController
 */
@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenService citizenService;
    private final CitizenResourceAssembler citizenResourceAssembler;
    private final CitizenCashbackResourceAssembler citizenCashbackResourceAssembler;
    private final ModelFactory<CitizenDTO, Citizen> citizenFactory;
    private final CitizenPatchFactory citizenPatchFactory;
    private final CitizenRankingResourceAssembler citizenRankingResourceAssembler;
    private final CitizenRankingMilestoneResourceAssembler citizenRankingMilestoneResourceAssembler;


    @Autowired
    public BpdCitizenControllerImpl(CitizenService citizenService,
                                    CitizenResourceAssembler citizenResourceAssembler,
                                    ModelFactory<CitizenDTO, Citizen> citizenFactory,
                                    CitizenPatchFactory citizenPatchFactory,
                                    CitizenRankingResourceAssembler citizenRankingResourceAssembler,
                                    CitizenCashbackResourceAssembler citizenCashbackResourceAssembler, CitizenRankingMilestoneResourceAssembler citizenRankingMilestoneResourceAssembler) {
        this.citizenService = citizenService;
        this.citizenResourceAssembler = citizenResourceAssembler;
        this.citizenFactory = citizenFactory;
        this.citizenPatchFactory = citizenPatchFactory;
        this.citizenRankingResourceAssembler = citizenRankingResourceAssembler;
        this.citizenCashbackResourceAssembler = citizenCashbackResourceAssembler;
        this.citizenRankingMilestoneResourceAssembler = citizenRankingMilestoneResourceAssembler;
    }

    @Override
    public CitizenResource find(String fiscalCode, Boolean flagTechnicalAccount, Boolean isIssuer) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.find");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
        }

        final Citizen citizen = citizenService.find(fiscalCode);
        return citizenResourceAssembler.toCitizenResource(citizen, flagTechnicalAccount, isIssuer);
    }

    @Override
    public CitizenUpdateResource update(String fiscalCode, CitizenDTO citizen) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.update");
            logger.debug("fiscalCode = [" + fiscalCode + "], citizen = [" + citizen + "]");
        }

        final Citizen entity = citizenFactory.createModel(citizen);
        Citizen citizenEntity = citizenService.update(fiscalCode, entity);
        return citizenResourceAssembler.toCitizenUpdateResource(citizenEntity);
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
    public List<CitizenRankingResource> findRanking(String fiscalCode, Long awardPeriodId) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.findRanking");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
            logger.debug("awardPeriodId = [" + awardPeriodId + "]");
        }

        List<CitizenTransactionConverter> foundRanking = citizenService.findRankingDetails(fiscalCode, awardPeriodId);

        return citizenRankingResourceAssembler.toResource(foundRanking);
    }

    @Override
    public List<CitizenRankingMilestoneResource> findRankingMilestone(String fiscalCode, Long awardPeriodId) throws InvocationTargetException, IllegalAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.findRankingMilestone");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
            logger.debug("awardPeriodId = [" + awardPeriodId + "]");
        }

        List<CitizenTransactionMilestoneConverter> foundRanking = citizenService.findRankingMilestoneDetails(fiscalCode, awardPeriodId);

        return citizenRankingMilestoneResourceAssembler.toResource(foundRanking);
    }

    @Override
    public CitizenCashbackResource getTotalCashback(String fiscalCode, Long awardPeriodId) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdCitizenControllerImpl.getTotalCashback");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
            logger.debug("awardPeriodId = [" + awardPeriodId + "]");
        }

        CitizenRanking ranking = citizenService.getTotalCashback(getId(fiscalCode, awardPeriodId));
        return citizenCashbackResourceAssembler.toResource(ranking);
    }

    private CitizenRankingId getId(String fiscalCode, Long awardPeriodId) {
        CitizenRankingId id = new CitizenRankingId();
        id.setFiscalCode(fiscalCode);
        id.setAwardPeriodId(awardPeriodId);
        return id;
    }

}

