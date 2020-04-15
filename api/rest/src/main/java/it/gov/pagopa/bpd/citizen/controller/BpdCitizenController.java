package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Bonus Pagamenti Digitali Citizen Controller")
@RequestMapping("/bpd/citizens")
public interface BpdCitizenController {

    @GetMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource find(@PathVariable String fiscalCode);

    @PutMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource update(@PathVariable String fiscalCode, @RequestBody @Valid CitizenDTO citizen);

    @DeleteMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String fiscalCode);

}
