package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestConnector;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingReplicaDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
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
    private static final String EXISTING_FISCAL_CODE = "existing-fiscalCode";
    private static final String NOT_EXISTING_FISCAL_CODE = "not-existing-fiscalCode";

    @MockBean
    private CitizenDAO citizenDAOMock;
    @MockBean
    private CitizenRankingDAO citizenRankingDAOMock;
    @MockBean
    private CitizenRankingReplicaDAO citizenRankingReplicaDAOMock;
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

        Mockito.when(citizenRankingDAOMock.findById(Mockito.eq(new CitizenRankingId(EXISTING_FISCAL_CODE, 1L))))
                .thenAnswer((Answer<Optional<CitizenRanking>>)
                        invocation -> {
                            CitizenRanking citizenRanking = new CitizenRanking();
                            citizenRanking.setAwardPeriodId(1L);
                            citizenRanking.setFiscalCode(EXISTING_FISCAL_CODE);
                            citizenRanking.setTotalCashback(rankingMock);
                            return Optional.of(citizenRanking);
                        });

        Mockito.when(citizenRankingDAOMock.findById(Mockito.eq(new CitizenRankingId("wrongFiscalCode", 0L))))
                .thenAnswer((Answer<CitizenRanking>)
                        invocation -> null);

        Mockito.when(checkIbanRestConnectorMock.checkIban("testOK", EXISTING_FISCAL_CODE)).thenAnswer(
                (Answer<String>) invocation -> "OK");

        Mockito.when(checkIbanRestConnectorMock.checkIban("testKO", EXISTING_FISCAL_CODE)).thenAnswer(
                (Answer<String>) invocation -> "KO");

        Mockito.when(citizenRankingReplicaDAOMock.getRanking(Mockito.eq(EXISTING_FISCAL_CODE), Mockito.anyLong()))
                .thenAnswer((Answer<List<CitizenTransactionConverter>>)
                        invocation -> {
                            List<CitizenTransactionConverter> converter = new ArrayList<CitizenTransactionConverter>();
                            CitizenTransactionConverter item = new CitizenTransactionConverter() {
                                @Override
                                public Long getRanking() {
                                    return 1L;
                                }

                                @Override
                                public Long getTotalParticipants() {
                                    return 10L;
                                }

                                @Override
                                public Long getMaxTrxNumber() {
                                    return 11L;
                                }

                                @Override
                                public Long getMinTrxNumber() {
                                    return 1L;
                                }

                                @Override
                                public Long getTrxNumber() {
                                    return 2L;
                                }

                                @Override
                                public Long getAwardPeriodId() {
                                    return 1L;
                                }
                            };
                            converter.add(item);

                            return converter;
                        });

        Mockito.when(citizenRankingReplicaDAOMock.getRanking(Mockito.eq("wrongFiscalCode"), Mockito.eq(0L)))
                .thenAnswer((Answer<List<CitizenTransactionConverter>>)
                        invocation -> new ArrayList());
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
    public void getTotalCashback() {
        CitizenRankingId id = new CitizenRankingId();
        id.setFiscalCode(EXISTING_FISCAL_CODE);
        id.setAwardPeriodId(1L);
        Optional<CitizenRanking> citizenRanking = citizenRankingDAOMock.findById(id);

        Assert.assertNotNull(citizenRanking);
        BDDMockito.verify(citizenRankingDAOMock).findById(id);
        BDDMockito.verifyNoMoreInteractions(citizenRankingDAOMock);
    }

    @Test
    public void getTotalCashback_KO() {
        CitizenRankingId id = new CitizenRankingId();
        id.setFiscalCode("wrongFiscalCode");
        id.setAwardPeriodId(0L);
        Optional<CitizenRanking> citizenRanking = citizenRankingDAOMock.findById(id);

        Assert.assertNull(citizenRanking);
        BDDMockito.verify(citizenRankingDAOMock).findById(id);
        BDDMockito.verifyZeroInteractions(citizenRankingDAOMock);
    }

    @Test
    public void findRankingDetails() {
        List<CitizenTransactionConverter> converter = citizenRankingReplicaDAOMock.getRanking(
                EXISTING_FISCAL_CODE, 1L);

        Assert.assertNotNull(converter);
        BDDMockito.verify(citizenRankingReplicaDAOMock).getRanking(EXISTING_FISCAL_CODE, 1L);
        BDDMockito.verifyNoMoreInteractions(citizenRankingDAOMock);
    }

    @Test
    public void findRankingDetails_KO() {
        List<CitizenTransactionConverter> converter = citizenRankingReplicaDAOMock.getRanking(
                "wrongFiscalCode", 0L);

        BDDMockito.verify(citizenRankingReplicaDAOMock).getRanking("wrongFiscalCode", 0L);
        BDDMockito.verifyZeroInteractions(citizenRankingDAOMock);
    }


}