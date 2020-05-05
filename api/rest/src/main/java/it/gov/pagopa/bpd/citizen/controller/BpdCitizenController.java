package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
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
    CitizenResource find(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable
            @Valid @NotBlank
                    String fiscalCode
    );

    @PutMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource update(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable
            @Valid @NotBlank
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.timestampTC}", required = true)
            @RequestBody
                    CitizenDTO citizen
    );

    @PatchMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource updatePaymentMethod(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable
            @Valid @NotBlank
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.payoffInstr}", required = true)
            @RequestBody
            @Valid
                    CitizenPatchDTO citizen
    );

    @DeleteMapping(value = "/citizens/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable
            @Valid @NotBlank
                    String fiscalCode
    );

    @GetMapping(value = "/citizens/{fiscalCode}/ranking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenRankingResource findRanking(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable
            @Valid @NotBlank
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.awardPeriodId}", required = true)
            @RequestParam(value = "awardPeriodId")
                    Long awardPeriodId
    );

}
