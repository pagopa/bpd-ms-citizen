package it.gov.pagopa.bpd.citizen.command;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.TransactionCommandModel;
import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import it.gov.pagopa.bpd.citizen.service.PointTransactionPublisherService;
import it.gov.pagopa.bpd.common.BaseTest;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Test class for the FilterTransactionCommand method
 */

public class FilterTransactionCommandTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    CitizenService citizenServiceMock;
    @Mock
    PointTransactionPublisherService pointTransactionProducerServiceMock;


    @Before
    public void initTest() {
        Mockito.reset(
                citizenServiceMock,
                pointTransactionProducerServiceMock);
    }

    @Test
    public void test_BDPActive() {

        Citizen citizen = new Citizen();
        citizen.setFiscalCode("fiscalCode");
        citizen.setTimestampTC(OffsetDateTime.parse("2020-01-09T16:22:45.304Z"));
        Transaction transaction = getRequestObject();
        transaction.setFiscalCode(citizen.getFiscalCode());
        FilterTransactionCommand filterTransactionCommand = new FilterTransactionCommandImpl(
                TransactionCommandModel.builder().payload(transaction).build(),
                citizenServiceMock,
                pointTransactionProducerServiceMock
        );


        try {

            BDDMockito.doReturn(citizen).when(citizenServiceMock)
                    .find(Mockito.eq(transaction.getFiscalCode()));
            BDDMockito.doNothing().when(pointTransactionProducerServiceMock)
                    .publishPointTransactionEvent(Mockito.eq(transaction),Mockito.any());

            Boolean isOk = filterTransactionCommand.execute();

            Assert.assertTrue(isOk);
            BDDMockito.verify(citizenServiceMock, Mockito.atLeastOnce())
                    .find(Mockito.eq(transaction.getFiscalCode()));
            BDDMockito.verify(pointTransactionProducerServiceMock, Mockito.atLeastOnce())
                    .publishPointTransactionEvent(Mockito.any(),Mockito.any());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    protected FilterTransactionCommand buildCommandInstance(Transaction transaction) {
        return new FilterTransactionCommandImpl(
                TransactionCommandModel.builder().payload(transaction).headers(null).build(),
                citizenServiceMock,
                pointTransactionProducerServiceMock

        );
    }

    protected Transaction getRequestObject() {
        return Transaction.builder()
                .idTrxAcquirer("1")
                .acquirerCode("001")
                .trxDate(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .amount(BigDecimal.valueOf(1313.13))
                .operationType("00")
                .hpan("hpan")
                .merchantId("0")
                .circuitType("00")
                .mcc("813")
                .idTrxIssuer("0")
                .amountCurrency("833")
                .correlationId("1")
                .acquirerId("0")
                .terminalId("0")
                .bin("000004")
                .build();
    }

    @SneakyThrows
    @Test
    public void testExecute_KO_Validation() {

        Transaction transaction = getRequestObject();
        transaction.setAcquirerCode(null);
        FilterTransactionCommand filterTransactionCommand = buildCommandInstance(transaction);

        BDDMockito.doThrow(new RuntimeException("Some Exception")).when(citizenServiceMock)
                .find(Mockito.eq(transaction.getFiscalCode()));

        expectedException.expect(Exception.class);
        filterTransactionCommand.execute();

        BDDMockito.verifyZeroInteractions(citizenServiceMock);
        BDDMockito.verifyZeroInteractions(pointTransactionProducerServiceMock);

    }

    @Test
    public void testExecute_KO_Null() {

        FilterTransactionCommand filterTransactionCommand = buildCommandInstance(null);

        try {

            expectedException.expect(AssertionError.class);
            filterTransactionCommand.execute();
            BDDMockito.verifyZeroInteractions(citizenServiceMock);
            BDDMockito.verifyZeroInteractions(pointTransactionProducerServiceMock);
            BDDMockito.verifyZeroInteractions();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }
}
