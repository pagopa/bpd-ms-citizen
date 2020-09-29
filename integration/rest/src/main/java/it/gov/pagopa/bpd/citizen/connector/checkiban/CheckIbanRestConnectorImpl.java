package it.gov.pagopa.bpd.citizen.connector.checkiban;

import it.gov.pagopa.bpd.citizen.connector.checkiban.model.CheckIbanDTO;
import it.gov.pagopa.bpd.citizen.connector.checkiban.model.CheckIbanResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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

        CheckIbanResource response = checkIbanRestClient.checkIban(request, apikey, authSchema);
        if(response!=null && "OK".equals(response.getStatus()) && response.getPayload()!=null){
            return response.getPayload().getValidationStatus();
        }
        return null;
    }
}
