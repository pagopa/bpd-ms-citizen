package it.gov.pagopa.bpd.citizen.connector.checkiban;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import it.gov.pagopa.bpd.citizen.connector.checkiban.model.CheckIbanDTO;
import it.gov.pagopa.bpd.citizen.connector.checkiban.model.CheckIbanResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
import java.util.HashMap;

public class CheckIbanRestClient{

    private static final String IBAN="IBAN";
    private static final String PERSON_NATURAL="PERSON_NATURAL";

    private RestTemplate restTemplate;

    @Value("${rest-client.checkiban.base-url:https://bankingservices-sandbox.pagopa.it}${rest-client.checkiban.url:/api/pagopa/banking/v4.0/utils/validate-account-holder}")
    private String checkIbanURL;

    @Value("${rest-client.checkiban.apikey:ZPMNABCEGPR0D0VZZ64XBOU30REFPOLDTW}")
    private String checkIbanApiKey;

    public CheckIbanRestClient(){
        this.restTemplate= new RestTemplate();
    }

    public String checkIban(String iban, @Valid String fiscalCode){
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("Auth-Schema","S2S");
        requestHeader.add("apikey",checkIbanApiKey);

        HashMap<String, String> uriVariable = new HashMap<String,String>();

        CheckIbanDTO request = new CheckIbanDTO();
        request.getAccount().setValue(iban);
        request.getAccount().setValueType(IBAN);
        request.getAccountHolder().setFiscalCode(fiscalCode);
        request.getAccountHolder().setType(PERSON_NATURAL);

        ResponseEntity<CheckIbanResource> response = restTemplate.getForEntity(checkIbanURL,CheckIbanResource.class,uriVariable);

        CheckIbanResource body = response.getBody();
        if(body!=null && "OK".equals(body.getStatus()) && body.getPayload()!=null){
            return body.getPayload().getValidationStatus();
        }
        return null;
    }
}
