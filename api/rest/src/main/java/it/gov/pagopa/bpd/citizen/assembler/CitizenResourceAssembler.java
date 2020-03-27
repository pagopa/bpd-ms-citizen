package it.gov.pagopa.bpd.citizen.assembler;

import eu.sia.meda.core.assembler.BaseResourceAssemblerSupport;
import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import it.gov.pagopa.bpd.citizen.model.resource.CitizenResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CitizenResourceAssembler extends BaseResourceAssemblerSupport<Citizen, CitizenResource> {

    public CitizenResourceAssembler() {
        super(Citizen.class, CitizenResource.class);
    }

    @Override
    public CitizenResource toResource(Citizen citizen) {
        if (citizen == null) {
            return null;
        }
        CitizenResource resource = new CitizenResource();

        BeanUtils.copyProperties(citizen, resource);
        return resource;
    }
}
