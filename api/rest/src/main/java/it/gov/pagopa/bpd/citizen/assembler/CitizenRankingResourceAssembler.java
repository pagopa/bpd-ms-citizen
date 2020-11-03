package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import org.springframework.stereotype.Service;

/**
 * Mapper between <CitizenRanking> Entity class and <CitizenRankingResource> Resource class
 */
@Service
public class CitizenRankingResourceAssembler {

    public CitizenRankingResource toResource(CitizenTransactionConverter citizenTransaction) {
        CitizenRankingResource resource = null;

        if (citizenTransaction != null) {
            resource = new CitizenRankingResource();
            resource.setRanking(citizenTransaction.getRanking());
            resource.setTotalParticipants(citizenTransaction.getTotalParticipants());
            resource.setMaxTransactionNumber(citizenTransaction.getMaxTrxNumber());
            resource.setMinTransactionNumber(citizenTransaction.getMinTrxNumber());
            resource.setTransactionNumber(citizenTransaction.getTrxNumber());
            resource.setAwardPeriodId(citizenTransaction.getAwardPeriodId());
        }

        return resource;
    }
}
