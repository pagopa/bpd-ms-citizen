package it.gov.pagopa.bpd.citizen.publisher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
public class PaymentInstrumentUpdate {

    @NotNull
    @NotBlank
    String hpan;

    @NotNull
    @NotBlank
    String par;
}
