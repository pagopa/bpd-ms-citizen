package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.citizen.model.*;
import it.gov.pagopa.bpd.common.annotation.UpperCase;
import it.gov.pagopa.bpd.common.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali Citizen Controller")
@RequestMapping("/bpd/citizens")
@Validated
public interface BpdCitizenController {

    @GetMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource find(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.flagTechnicalAccount}")
            @RequestParam(value = "flagTechnicalAccount", required = false)
                    Boolean flagTechnicalAccount,
            @RequestParam(value = "isIssuer", required = false)
                    Boolean isIssuer
    );

    @PutMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenUpdateResource update(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)

                    String fiscalCode,
            @RequestBody @Valid CitizenDTO citizen);

    @PatchMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenPatchResource updatePaymentMethod(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @RequestBody @Valid CitizenPatchDTO citizen);

    @DeleteMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode
    );

    @GetMapping(value = "/{fiscalCode}/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<CitizenRankingResource> findRanking(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.awardPeriodId}", required = false)
            @RequestParam(value = "awardPeriodId", required = false)
                    Long awardPeriodId
    );

    @GetMapping(value = "/{fiscalCode}/ranking/milestone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<CitizenRankingMilestoneResource> findRankingMilestone(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @PathVariable @UpperCase
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.awardPeriodId}")
            @RequestParam(value = "awardPeriodId", required = false)
                    Long awardPeriodId
    ) throws InvocationTargetException, IllegalAccessException;

    @GetMapping(value = "/total-cashback", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenCashbackResource getTotalCashback(
            @ApiParam(value = "${swagger.citizen.fiscalCode}", required = true)
            @NotBlank
            @RequestParam
                    String fiscalCode,
            @ApiParam(value = "${swagger.citizen.awardPeriodId}", required = true)
            @NotNull
            @RequestParam
                    Long awardPeriodId

    );

}




