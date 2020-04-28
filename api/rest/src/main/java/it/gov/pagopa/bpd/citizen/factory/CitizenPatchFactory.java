package it.gov.pagopa.bpd.citizen.factory;

import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper between <CitizenPatchDTO> DTO class and <Citizen> Entity class
 */
@Component
public class CitizenPatchFactory implements ModelFactory<CitizenPatchDTO, Citizen> {

    @Override
    public Citizen createModel(CitizenPatchDTO dto) {
        final Citizen result = new Citizen();

        result.setPayoffInstr(dto.getPayoffInstr());
        result.setPayoffInstrType(dto.getPayoffInstrType());

        return result;
    }

}
