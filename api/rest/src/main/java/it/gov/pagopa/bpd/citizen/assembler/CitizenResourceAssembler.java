package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.model.CitizenUpdateResource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Mapper between <Citizen> Entity class and <CitizenResource> Resource class
 */
@PropertySource("classpath:/config/citizen.properties")
@Service
public class CitizenResourceAssembler {

    @Value("${citizen.resource.assembler.technical.account.holder.placeholder}")
    private String TECHNICAL_ACCOUNT_HOLDER_PLACEHOLDER;
    @Value("${citizen.resource.assembler.technical.account.holder.placeholder.not.issuer}")
    private String TECHNICAL_ACCOUNT_HOLDER_PLACEHOLDER_NOT_ISSUER;
    @Value("${citizen.resource.assembler.payoff.instr.placeholder}")
    private String PAYOFF_INSTR_PLACEHOLDER;

    public CitizenResource toCitizenResource(Citizen citizen, Boolean flagTechnicalAccount, Boolean isIssuer) {
        CitizenResource resource = null;

        if (citizen != null) {
            resource = new CitizenResource();
            BeanUtils.copyProperties(citizen, resource);

            if (isIssuer != null && isIssuer && citizen.getTechnicalAccountHolder() != null) {
                    resource.setTechnicalAccount(null);
                if (flagTechnicalAccount == null || !flagTechnicalAccount) {
                    resource.setTechnicalAccountHolder(null);
                    resource.setIssuerCardId(null);
                }  else {
                    resource.setPayoffInstr(PAYOFF_INSTR_PLACEHOLDER);
                }
            } else if((isIssuer == null || !isIssuer) && citizen.getTechnicalAccountHolder() != null){
                resource.setTechnicalAccount(TECHNICAL_ACCOUNT_HOLDER_PLACEHOLDER_NOT_ISSUER
                        +citizen.getTechnicalAccountHolder());
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
