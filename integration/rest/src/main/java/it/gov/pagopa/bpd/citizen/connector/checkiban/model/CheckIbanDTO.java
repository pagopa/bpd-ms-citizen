package it.gov.pagopa.bpd.citizen.connector.checkiban.model;

import lombok.Data;

@Data
public class CheckIbanDTO{

    private Account account = new Account();
    private AccountHolder accountHolder = new AccountHolder();

    @Data
    public class Account{
        private String value;
        private String valueType;
        private String bicCode;
    }

    @Data
    public class AccountHolder{
        private String type;
        private String fiscalCode;
        private String vatCode;
        private String taxCode;
    }

    @Override
    public String toString(){
        return "{'fiscalCode': '" + accountHolder.getFiscalCode()
                + "', 'IBAN': '" + account.getValue() + "'}";
    }
}