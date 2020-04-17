package it.gov.pagopa.bpd.citizen.factory;

import it.gov.pagopa.bpd.citizen.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import org.springframework.stereotype.Component;

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
