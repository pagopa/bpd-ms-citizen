package it.gov.pagopa.bpd.citizen.model.resource;

import eu.sia.meda.core.resource.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.core.Relation;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(value = "citizenResource", collectionRelation = "citizenResources")
public class CitizenResource extends BaseResource {

    private String fiscalCode;
    private String payoffInstr;
    private String payoffInstrType;
    private Date timestamp;

}
