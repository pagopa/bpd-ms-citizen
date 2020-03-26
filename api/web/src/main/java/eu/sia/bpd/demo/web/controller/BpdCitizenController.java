package eu.sia.bpd.demo.web.controller;

import eu.sia.bpd.demo.web.controller.model.dto.CitizenDTO;
import eu.sia.bpd.demo.model.Citizen;
import eu.sia.meda.core.controller.CrudController;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(tags = "Bonus Pagamenti Digitali Citizen Controller")
@RequestMapping("citizens")
public interface BpdCitizenController {

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Citizen insert(@RequestBody CitizenDTO citizen);

    @GetMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Optional<Citizen> find(@PathVariable String fiscalCode);

    @PutMapping(value = "{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Citizen update(@RequestBody CitizenDTO citizen);

    @DeleteMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void delete(@PathVariable String fiscalCode);

}
