package it.gov.pagopa.bpd.citizen.assembler;

import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionMilestoneConverter;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingMilestoneResource;
import it.gov.pagopa.bpd.citizen.model.MilestoneResource;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper between <CitizenRanking> Entity class and <CitizenRankingMilestoneResource> Resource class
 */
@Service
public class CitizenRankingMilestoneResourceAssembler {

    public List<CitizenRankingMilestoneResource> toResource(List<CitizenTransactionMilestoneConverter> citizenTransaction) throws InvocationTargetException, IllegalAccessException {
        List<CitizenRankingMilestoneResource> resource = null;

        if (citizenTransaction != null) {
            resource = new ArrayList<>();
            for (CitizenTransactionConverter citizenTransactionConverter : citizenTransaction) {
                CitizenRankingMilestoneResource item = new CitizenRankingMilestoneResource();
                item.setRanking(citizenTransactionConverter.getRanking() == null ?
                        citizenTransactionConverter.getTotalParticipants() + 1 :
                        citizenTransactionConverter.getRanking());
                item.setTotalParticipants(citizenTransactionConverter.getTotalParticipants());
                item.setMaxTransactionNumber(citizenTransactionConverter.getMaxTrxNumber());
                item.setMinTransactionNumber(citizenTransactionConverter.getMinTrxNumber());
                item.setTransactionNumber(citizenTransactionConverter.getTrxNumber() == null ?
                        0 :
                        citizenTransactionConverter.getTrxNumber());
                item.setAwardPeriodId(citizenTransactionConverter.getAwardPeriodId());

                MilestoneResource milestoneResource = new MilestoneResource();
                BeanUtils.copyProperties(resource, citizenTransactionConverter);
                item.setMilestoneResource(milestoneResource);

                resource.add(item);
            }
        }

        return resource;
    }
}
