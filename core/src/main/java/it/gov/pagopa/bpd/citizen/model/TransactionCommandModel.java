package it.gov.pagopa.bpd.citizen.model;

import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;

/**
 * Model containing the inbound message data
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCommandModel {

    private Transaction payload;
    private Headers headers;

}
