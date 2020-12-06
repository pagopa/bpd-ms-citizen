package it.gov.pagopa.bpd.citizen.connector.checkiban;

import it.gov.pagopa.bpd.citizen.connector.checkiban.model.CheckIbanDTO;
import it.gov.pagopa.bpd.citizen.connector.checkiban.model.CheckIbanResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckIbanRestConnectorImpl implements CheckIbanRestConnector{

    private final String apikey;
    private final String authSchema;
    private final CheckIbanRestClient checkIbanRestClient;
    private static final String IBAN="IBAN";
    private static final String PERSON_NATURAL="PERSON_NATURAL";

    public CheckIbanRestConnectorImpl(
            @Value("${rest-client.checkiban.apikey}") String apikey,
            @Value("${rest-client.checkiban.authSchema}") String authSchema,
            CheckIbanRestClient checkIbanRestClient) {
        this.apikey = apikey;
        this.authSchema = authSchema;
        this.checkIbanRestClient = checkIbanRestClient;
    }


    @Override
    public String checkIban(String payOffInstr, String fiscalCode) {
        CheckIbanDTO request = new CheckIbanDTO();
        request.getAccount().setValue(payOffInstr);
        request.getAccount().setValueType(IBAN);
        request.getAccountHolder().setFiscalCode(fiscalCode);
        request.getAccountHolder().setType(PERSON_NATURAL);

        if(log.isInfoEnabled()){
            log.info("CHECKIBAN_TO: " + request.toString());
        }
        CheckIbanResource response = checkIbanRestClient.checkIban(request, apikey, authSchema);

        if(log.isInfoEnabled()){
            log.info("CHECKIBAN_FROM: " + (response!=null ? response.toString() : "[null]"));
        }

        if(response!=null && "OK".equals(response.getStatus()) && response.getPayload()!=null){
            return response.getPayload().getValidationStatus();
        }
        return null;
    }
}
