package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.model.CitizenCashbackResource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class CitizenCashbackResourceAssembler {

    public CitizenCashbackResource toResource(CitizenRanking citizenCashback) {
        CitizenCashbackResource resource = new CitizenCashbackResource();

        if (citizenCashback != null) {
            resource.setTotalCashback(
                (citizenCashback.getTotalCashback()!=null
                        && citizenCashback.getMaxTotalCashback()!=null
                        && citizenCashback.getTotalCashback().compareTo(citizenCashback.getMaxTotalCashback()) == 1) ?
                            citizenCashback.getMaxTotalCashback() : citizenCashback.getTotalCashback());
            resource.setTransactionNumber(citizenCashback.getTransactionNumber());

        }else{
            resource.setTotalCashback(new BigDecimal(0L));
            resource.setTransactionNumber(new Long(0));
        }

        return resource;
    }
}
