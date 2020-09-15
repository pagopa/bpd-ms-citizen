package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.common.annotation.UpperCase;
import it.gov.pagopa.bpd.common.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali Citizen Controller")
@RequestMapping("/bpd/citizens")
@Validated
public interface BpdCitizenController {

    @GetMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource find(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode
    );

    @PutMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource update(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)

                    String fiscalCode,
            @RequestBody @Valid CitizenDTO citizen);

    @PatchMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updatePaymentMethod(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @RequestBody @Valid CitizenPatchDTO citizen);

    @DeleteMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode
    );

    @GetMapping(value = "/{fiscalCode}/ranking", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenRankingResource findRanking(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.awardPeriodId}", required = true)
            @RequestParam(value = "awardPeriodId")
                    Long awardPeriodId
    );


}




