package it.gov.pagopa.bpd.citizen.connector.checkiban;

import org.springframework.stereotype.Service;

@Service
public interface CheckIbanRestConnector {
    String checkIban(String payOffInstr, String fiscalCode);
}
