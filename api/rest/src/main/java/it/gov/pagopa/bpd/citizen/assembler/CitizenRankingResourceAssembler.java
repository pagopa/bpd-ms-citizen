package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import org.springframework.stereotype.Service;

/**
 * Mapper between <CitizenRanking> Entity class and <CitizenRankingResource> Resource class
 */
@Service
public class CitizenRankingResourceAssembler {

    public CitizenRankingResource toResource(CitizenRanking citizenRanking, Long attendeesNumber) {
        CitizenRankingResource resource = null;

        if (citizenRanking != null && attendeesNumber != null) {
            resource = new CitizenRankingResource();
            resource.setRanking(citizenRanking.getRanking());
            resource.setTotalParticipants(attendeesNumber);
        }

        return resource;
    }
}
