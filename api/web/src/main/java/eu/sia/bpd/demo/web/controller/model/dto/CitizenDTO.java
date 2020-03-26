package eu.sia.bpd.demo.web.controller.model.dto;


import eu.sia.bpd.demo.model.Citizen;
import lombok.Data;

import java.util.Date;

@Data
public class CitizenDTO extends ReflectionDTO<Citizen> {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private Date timestamp;
}
