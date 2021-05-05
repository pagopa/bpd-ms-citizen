package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.AwardWinner;
import it.gov.pagopa.bpd.citizen.model.AwardWinnerResource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper between <AwardWinner> Entity class and <AwardWinnerResource> Resource class
 */
@Service
public class AwardWinnerResourceAssembler {

    public List<AwardWinnerResource> toResource(List<AwardWinner> awardWinnerList) {
        List<AwardWinnerResource> resource = null;

        if (awardWinnerList != null && !awardWinnerList.isEmpty()) {
            resource = new ArrayList<AwardWinnerResource>();

            for(AwardWinner aw : awardWinnerList){
                AwardWinnerResource item = new AwardWinnerResource();
                BeanUtils.copyProperties(aw, item);
                resource.add(item);
            }
        }

        return resource;
    }
}
