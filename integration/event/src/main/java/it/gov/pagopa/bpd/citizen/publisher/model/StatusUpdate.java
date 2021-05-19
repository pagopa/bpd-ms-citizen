package it.gov.pagopa.bpd.citizen.publisher.model;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"fiscalCode", "enabled", "updateDateTime"}, callSuper = false)
public class StatusUpdate {

    private String fiscalCode;
    private boolean enabled;
    private OffsetDateTime updateDateTime;

}
