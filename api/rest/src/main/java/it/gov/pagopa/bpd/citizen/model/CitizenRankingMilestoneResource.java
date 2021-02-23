package it.gov.pagopa.bpd.citizen.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CitizenRankingMilestoneResource extends CitizenRankingResource{
    private MilestoneResource milestoneResource;
}
