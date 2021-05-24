package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.GetTotalCashbackResource;
import it.gov.pagopa.bpd.citizen.model.CitizenCashbackResource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class CitizenCashbackResourceAssembler {

    public CitizenCashbackResource toResource(GetTotalCashbackResource citizenCashback) {
        CitizenCashbackResource resource = new CitizenCashbackResource();
        resource.setTotalCashback(new BigDecimal(0L));
        resource.setTransactionNumber(0L);

        if (citizenCashback != null) {
            if (citizenCashback.getTotalCashback() != null) {
                resource.setTotalCashback(
                        (citizenCashback.getMaxTotalCashback() != null
                                && citizenCashback.getTotalCashback().compareTo(citizenCashback.getMaxTotalCashback()) == 1) ?
                                citizenCashback.getMaxTotalCashback() : citizenCashback.getTotalCashback());
            }
            if (citizenCashback.getTransactionNumber() != null) {
                resource.setTransactionNumber(citizenCashback.getTransactionNumber());
            }
        }

        return resource;
    }
}
