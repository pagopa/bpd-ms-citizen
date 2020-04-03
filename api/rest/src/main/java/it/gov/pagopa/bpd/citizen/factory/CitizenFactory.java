package it.gov.pagopa.bpd.citizen.factory;

import it.gov.pagopa.bpd.citizen.model.dto.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.entity.Citizen;
import org.springframework.stereotype.Component;

@Component
class CitizenFactory implements ModelFactory<CitizenDTO, Citizen> {

    @Override
    public Citizen createModel(CitizenDTO dto) {
        final Citizen result = new Citizen();

        result.setTimestamp_tc(dto.getTimestamp_tc());

        return result;
    }

}
