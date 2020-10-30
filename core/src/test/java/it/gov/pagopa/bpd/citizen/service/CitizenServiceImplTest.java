package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestConnector;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenCashbackDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.*;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.CashbackResource;
import it.gov.pagopa.bpd.citizen.exception.CitizenNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CitizenServiceImpl.class)
public class CitizenServiceImplTest {

    private final OffsetDateTime DATE = OffsetDateTime.now();
    private final Long attendeesNumberMock = new Random().nextLong();
    private final BigDecimal rankingMock = new BigDecimal(100);
    private static final String HASH_PAN = "hpan";
    private static final String EXISTING_FISCAL_CODE = "existing-fiscalCode";
    private static final String NOT_EXISTING_FISCAL_CODE = "not-existing-fiscalCode";

    @MockBean
    private CitizenDAO citizenDAOMock;
    @MockBean
    private CitizenRankingDAO citizenRankingDAOMock;
    @MockBean
    private CitizenTransactionDAO citizenTransactionDAOMock;
    @MockBean
    private CitizenCashbackDAO citizenCashbackDAOMock;
    @MockBean
    private CheckIbanRestConnector checkIbanRestConnectorMock;
    @Autowired
    private CitizenService citizenService;


    @Before
    public void initTest() {
        Mockito.reset(citizenDAOMock);

        when(citizenDAOMock.findById(anyString()))
                .thenAnswer(invocation -> {
                    String fiscalCode = invocation.getArgument(0, String.class);
                    Optional<Citizen> result = Optional.empty();
                    if (EXISTING_FISCAL_CODE.equals(fiscalCode)) {
                        Citizen citizen = new Citizen();
                        citizen.setFiscalCode(fiscalCode);
                        result = Optional.of(citizen);
                    }
                    return result;
                });

        Mockito.when(citizenRankingDAOMock.findById(Mockito.eq(new CitizenRankingId(HASH_PAN, EXISTING_FISCAL_CODE, 0L))))
                .thenAnswer((Answer<Optional<CitizenRanking>>)
                        invocation -> {
                            CitizenRanking citizenRanking = new CitizenRanking();
                            citizenRanking.setAwardPeriodId(0L);
                            citizenRanking.setFiscalCode(EXISTING_FISCAL_CODE);
                            citizenRanking.setTotalCashback(rankingMock);
                            return Optional.of(citizenRanking);
                        });

        Mockito.when(citizenDAOMock.count()).thenAnswer((Answer<Long>)
                invocation -> attendeesNumberMock);

        Mockito.when(citizenTransactionDAOMock.getTransactionDetails(EXISTING_FISCAL_CODE, 1L))
                .thenAnswer(
                        invocation -> {
                            CitizenTransaction trx = new CitizenTransaction();
                            trx.setMaxTrx(2L);
                            trx.setMinTrx(1L);
                            trx.setTotalTrx(1L);
                            return trx;

                        });

        Mockito.when(citizenRankingDAOMock.findById(Mockito.eq(new CitizenRankingId("hpan", "wrongFiscalCode", 0L))))
                .thenAnswer((Answer<CitizenRanking>)
                        invocation -> null);

        Mockito.when(checkIbanRestConnectorMock.checkIban("testOK", EXISTING_FISCAL_CODE)).thenAnswer(
                (Answer<String>) invocation -> "OK");

        Mockito.when(checkIbanRestConnectorMock.checkIban("testKO", EXISTING_FISCAL_CODE)).thenAnswer(
                (Answer<String>) invocation -> "KO");

        Mockito.when(citizenCashbackDAOMock.getTotalCashback(Mockito.eq(EXISTING_FISCAL_CODE), Mockito.eq(1L)))
                .thenAnswer(invocation -> {
                    List<CitizenCashback> list = new ArrayList<>();
                    CitizenCashback cashback = new CitizenCashback();
                    cashback.setTotalCashback(new BigDecimal(100));
                    cashback.setTransactionNumber(10L);
                    list.add(cashback);
                    return list;
                });
    }


    @Test
    public void find() {
        Citizen citizen = citizenService.find(EXISTING_FISCAL_CODE);

        Assert.assertNotNull(citizen);
        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq(EXISTING_FISCAL_CODE));
    }

    @Test(expected = CitizenNotFoundException.class)
    public void find_KO() {
        try {
            Citizen citizen = citizenService.find(NOT_EXISTING_FISCAL_CODE);
        } finally {
            verify(citizenDAOMock, only()).findById(eq(NOT_EXISTING_FISCAL_CODE));
            verify(citizenDAOMock, times(1)).findById(eq(NOT_EXISTING_FISCAL_CODE));
        }
    }

    @Test
    public void update() {
        Citizen citizen = new Citizen();
        citizenService.update("test", citizen);

        Assert.assertNotNull(citizen);
        BDDMockito.verify(citizenDAOMock).save(Mockito.eq(citizen));
    }

    @Test
    public void delete() {
        citizenService.delete(EXISTING_FISCAL_CODE);

        BDDMockito.verify(citizenDAOMock).save(Mockito.any(Citizen.class));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void patch() {
        Citizen citizen = new Citizen();
        citizen.setPayoffInstr("testOK");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizen.setAccountHolderCF("DTUMTO13I14I814Z");
        citizen.setAccountHolderName("accountHolderName");
        citizen.setAccountHolderSurname("accountHolderSurname");
        citizen.setFiscalCode(EXISTING_FISCAL_CODE);
        citizenService.patch(EXISTING_FISCAL_CODE, citizen);
        citizen.setUpdateUser(EXISTING_FISCAL_CODE);

        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq(EXISTING_FISCAL_CODE));
        BDDMockito.verify(citizenDAOMock).save(Mockito.eq(citizen));
        BDDMockito.verify(checkIbanRestConnectorMock).checkIban(citizen.getPayoffInstr(),citizen.getFiscalCode());
    }

    @Test
    public void patch_KO() {
        Citizen citizen = new Citizen();
        citizen.setPayoffInstr("testKO");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizen.setAccountHolderCF("DTUMTO13I14I814Z");
        citizen.setAccountHolderName("accountHolderName");
        citizen.setAccountHolderSurname("accountHolderSurname");
        citizen.setFiscalCode(EXISTING_FISCAL_CODE);
        citizenService.patch(EXISTING_FISCAL_CODE, citizen);
        citizen.setUpdateUser(EXISTING_FISCAL_CODE);

        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq(EXISTING_FISCAL_CODE));
        BDDMockito.verifyZeroInteractions(citizenDAOMock);
        BDDMockito.verify(checkIbanRestConnectorMock).checkIban(citizen.getPayoffInstr(),citizen.getFiscalCode());
    }

    @Test(expected = CitizenNotFoundException.class)
    public void patch_NotFound() {
        Citizen citizen = new Citizen();
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizenService.patch(NOT_EXISTING_FISCAL_CODE, citizen);

        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq(NOT_EXISTING_FISCAL_CODE));
        BDDMockito.verifyNoMoreInteractions(citizenDAOMock);
    }


    @Test
    public void findRanking() {
        CitizenRanking citizenRanking = citizenService.findRanking(HASH_PAN, EXISTING_FISCAL_CODE, 0L);
        Assert.assertNotNull(citizenRanking);
        Assert.assertEquals(rankingMock, citizenRanking.getTotalCashback());
        BDDMockito.verify(citizenRankingDAOMock, Mockito.atLeastOnce())
                .findById(Mockito.eq(new CitizenRankingId(HASH_PAN, EXISTING_FISCAL_CODE, 0L)));
    }

    @Test
    public void calculateAttendeesNumber() {
        Long attendeesNumber = citizenService.calculateAttendeesNumber();

        Assert.assertEquals(attendeesNumberMock, attendeesNumber);
    }

    @Test
    public void getTransactionDetails() {
        CitizenTransaction citizenTransaction = citizenTransactionDAOMock.getTransactionDetails(EXISTING_FISCAL_CODE, 1L);

        Assert.assertNotNull(citizenTransaction);
        Assert.assertSame(citizenTransaction.getMaxTrx(), 2L);
        Assert.assertSame(citizenTransaction.getMinTrx(), 1L);
        Assert.assertSame(citizenTransaction.getTotalTrx(), 1L);
    }

    @Test
    public void getCashback() {
        List<CitizenCashback> citizenCashback = citizenCashbackDAOMock.getTotalCashback(EXISTING_FISCAL_CODE, 1L);
        CashbackResource resource = new CashbackResource();

        resource.setTotalCashback(citizenCashback.stream()
                .map(CitizenCashback::getTotalCashback).reduce(BigDecimal.ZERO, BigDecimal::add));
        resource.setTransactionNumber(citizenCashback.stream()
                .mapToLong(CitizenCashback::getTransactionNumber).sum());

        Assert.assertNotNull(resource);
        Assert.assertEquals(resource.getTotalCashback(), new BigDecimal(100));


    }


}