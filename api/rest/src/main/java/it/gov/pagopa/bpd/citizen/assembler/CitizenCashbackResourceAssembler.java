package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.model.CitizenCashbackResource;
import org.springframework.stereotype.Service;


@Service
public class CitizenCashbackResourceAssembler {

    public CitizenCashbackResource toResource(CitizenRanking citizenCashback) {
        CitizenCashbackResource resource = null;

        if (citizenCashback != null) {
            resource = new CitizenCashbackResource();
            resource.setTotalCashback(citizenCashback.getTotalCashback());
            resource.setTransactionNumber(citizenCashback.getTransactionNumber());

        }

        return resource;
    }
}
