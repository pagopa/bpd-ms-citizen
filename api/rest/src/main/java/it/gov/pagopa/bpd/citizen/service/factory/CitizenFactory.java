package it.gov.pagopa.bpd.citizen.service.factory;

import it.gov.pagopa.bpd.citizen.service.model.dto.CitizenDTO;
import it.gov.pagopa.bpd.citizen.service.model.entity.Citizen;
import org.springframework.stereotype.Component;

@Component
class CitizenFactory implements ModelFactory<CitizenDTO, Citizen> {

    @Override
    public Citizen createModel(CitizenDTO dto) {
        final Citizen result = new Citizen();

        result.setTimestampTc(dto.getTimestampTc());

        return result;
    }

}
