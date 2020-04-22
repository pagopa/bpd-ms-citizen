package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.FileNotFoundException;

@Api(tags = "Bonus Pagamenti Digitali Citizen Controller")
@RequestMapping("/bpd")
public interface BpdCitizenController {

    @GetMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource find(@PathVariable @Valid @NotBlank String fiscalCode);

    @PutMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource update(@PathVariable @Valid @NotBlank String fiscalCode, @RequestBody CitizenDTO citizen);

    @PatchMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource updatePaymentMethod(@PathVariable @Valid @NotBlank String fiscalCode, @RequestBody @Valid CitizenPatchDTO citizen);

    @PutMapping(value = "/enrollment/io/citizens/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void updateTC(@PathVariable("id") @Valid @NotBlank String fiscalCode, @RequestBody CitizenDTO citizen);

    @DeleteMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable @Valid @NotBlank String fiscalCode);

    @RequestMapping(value = "/citizens/tc", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getTermsAndConditions() throws FileNotFoundException;

}
