package it.gov.pagopa.bpd.citizen.controller;

import io.swagger.annotations.Api;
import it.gov.pagopa.bpd.citizen.model.dto.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.resource.CitizenResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Bonus Pagamenti Digitali Citizen Controller")
@RequestMapping("citizens")
public interface BpdCitizenController {

    @GetMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource find(@PathVariable String fiscalCode);

    @PutMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CitizenResource update(@PathVariable String fiscalCode, @RequestBody CitizenDTO citizen);

    @DeleteMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String fiscalCode);

}
