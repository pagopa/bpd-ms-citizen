package it.gov.pagopa.bpd.citizen.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.factory.ModelFactory;
import it.gov.pagopa.bpd.citizen.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
public class BpdCitizenControllerImpl extends StatelessController implements BpdCitizenController {

    private final CitizenService citizenService;
    private final CitizenResourceAssembler citizenResourceAssembler;
    private final ModelFactory<CitizenDTO, Citizen> citizenFactory;
    private final ModelFactory<CitizenPatchDTO, Citizen> citizenPatchFactory;


    @Autowired
    public BpdCitizenControllerImpl(CitizenService citizenService,
                                    CitizenResourceAssembler citizenResourceAssembler,
                                    ModelFactory<CitizenDTO, Citizen> citizenFactory, ModelFactory<CitizenPatchDTO, Citizen> citizenPatchFactory) {
        this.citizenService = citizenService;
        this.citizenResourceAssembler = citizenResourceAssembler;
        this.citizenFactory = citizenFactory;
        this.citizenPatchFactory = citizenPatchFactory;
    }

    @Override
    public CitizenResource find(String fiscalCode) {
        logger.debug("Start find by fiscal code");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Optional<Citizen> citizen = citizenService.find(fiscalCode);
        return citizenResourceAssembler.toResource(citizen.get());
    }

    @Override
    public CitizenResource update(String fiscalCode, CitizenDTO citizen) {
        logger.debug("Start update");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);
        Citizen citizenEntity = citizenService.update(fiscalCode, entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }

    @Override
    public CitizenResource updatePaymentMethod(String fiscalCode, CitizenPatchDTO citizen) {
        logger.debug("Start update");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Citizen entity = citizenPatchFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);
        entity.setPayoffInstr(citizen.getPayoffInstr());
        entity.setPayoffInstrType(citizen.getPayoffInstrType());
        Citizen citizenEntity = citizenService.patch(fiscalCode, entity);
        return citizenResourceAssembler.toResource(citizenEntity);
    }


    @Override
    public void delete(String fiscalCode) {
        logger.debug("Start delete");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        citizenService.delete(fiscalCode);
    }


    @Override
    public void updateTC(String fiscalCode, CitizenDTO citizen) {
        logger.debug("Start update T&C");
        logger.debug("fiscalCode = [" + fiscalCode + "]");

        final Citizen entity = citizenFactory.createModel(citizen);
        entity.setFiscalCode(fiscalCode);

        Citizen citizenEntity = citizenService.update(fiscalCode, entity);
    }

    @Override
    public ResponseEntity<InputStreamResource> tcReport() throws FileNotFoundException {
        byte[] file = citizenService.getPdf(OffsetDateTime.now());
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=terms&conditionreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(file)));
    }

}

