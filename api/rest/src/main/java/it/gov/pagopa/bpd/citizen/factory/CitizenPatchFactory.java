package it.gov.pagopa.bpd.citizen.factory;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Mapper between <CitizenPatchDTO> DTO class and <Citizen> Entity class
 */
@Component
public class CitizenPatchFactory implements ModelFactory<CitizenPatchDTO, Citizen> {

    @Override
    public Citizen createModel(CitizenPatchDTO dto) {
        final Citizen result = new Citizen();

        BeanUtils.copyProperties(dto, result);

        return result;
    }

}
