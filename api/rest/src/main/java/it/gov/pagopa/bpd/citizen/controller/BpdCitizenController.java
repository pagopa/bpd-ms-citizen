package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Controller to expose MicroService
 */
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

    @DeleteMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable @Valid @NotBlank String fiscalCode);

    @GetMapping(value = "/citizens/{fiscalCode}/ranking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenRankingResource findRanking(@PathVariable @Valid @NotBlank String fiscalCode,
                                       @RequestParam(value = "awardPeriodId") Long awardPeriodId);

}
