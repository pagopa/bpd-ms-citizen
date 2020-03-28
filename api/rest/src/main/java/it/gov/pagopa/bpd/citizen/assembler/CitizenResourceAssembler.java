package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import it.gov.pagopa.bpd.citizen.model.resource.CitizenResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CitizenResourceAssembler {

    public CitizenResource toResource(Citizen citizen) {
        CitizenResource resource = null;

        if (citizen != null) {
            resource = new CitizenResource();
            BeanUtils.copyProperties(citizen, resource);
        }

        return resource;
    }
}
