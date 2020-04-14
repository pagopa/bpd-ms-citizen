package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
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
