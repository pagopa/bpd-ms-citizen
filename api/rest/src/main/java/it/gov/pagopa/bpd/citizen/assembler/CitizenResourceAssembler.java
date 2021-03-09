package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.model.CitizenUpdateResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Mapper between <Citizen> Entity class and <CitizenResource> Resource class
 */
@Service
public class CitizenResourceAssembler {

    private final String TECHNICAL_ACCOUNT_HOLDER_PLACEHOLDER = "CONTO TECNICO";

    public CitizenResource toCitizenResource(Citizen citizen, Boolean flagTechnicalAccount) {
        CitizenResource resource = null;

        if (citizen != null) {
            resource = new CitizenResource();
            BeanUtils.copyProperties(citizen, resource);

            if (citizen.getTechnicalAccountHolder() != null && flagTechnicalAccount != null) {
                if(flagTechnicalAccount) {
                    resource.setPayoffInstr(TECHNICAL_ACCOUNT_HOLDER_PLACEHOLDER);
                } else{
                    resource.setTechnicalAccountHolder(null);
                    resource.setIssuerCardId(null);
                }
            }
        }

        return resource;
    }

    public CitizenUpdateResource toCitizenUpdateResource(Citizen citizen) {
        CitizenUpdateResource resource = null;

        if (citizen != null) {
            resource = new CitizenUpdateResource();
            BeanUtils.copyProperties(citizen, resource);

        }

        return resource;
    }
}
