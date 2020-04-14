package it.gov.pagopa.bpd.citizen.factory;

import it.gov.pagopa.bpd.citizen.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import org.springframework.stereotype.Component;

@Component
public class CitizenFactory implements ModelFactory<CitizenDTO, Citizen> {

    @Override
    public Citizen createModel(CitizenDTO dto) {
        final Citizen result = new Citizen();

        result.setTimestampTc(dto.getTimestampTc());

        return result;
    }

}
