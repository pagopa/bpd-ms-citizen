package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper between <CitizenRanking> Entity class and <CitizenRankingResource> Resource class
 */
@Service
public class CitizenRankingResourceAssembler {

    public List<CitizenRankingResource> toResource(List<CitizenTransactionConverter> citizenTransaction) {
        List<CitizenRankingResource> resource = null;

        if (citizenTransaction != null) {
            resource = new ArrayList<CitizenRankingResource>();
            for (CitizenTransactionConverter citizenTransactionConverter : citizenTransaction) {
                CitizenRankingResource item = new CitizenRankingResource();
                item.setRanking(citizenTransactionConverter.getRanking());
                item.setTotalParticipants(citizenTransactionConverter.getTotalParticipants());
                item.setMaxTransactionNumber(citizenTransactionConverter.getMaxTrxNumber());
                item.setMinTransactionNumber(citizenTransactionConverter.getMinTrxNumber());
                item.setTransactionNumber(citizenTransactionConverter.getTrxNumber());
                item.setAwardPeriodId(citizenTransactionConverter.getAwardPeriodId());

                resource.add(item);
            }
        }

        return resource;
    }
}
