package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.connector.checkiban.CheckIbanRestConnector;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenDAO;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenRankingDAO;
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

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CitizenServiceImpl.class)
public class CitizenServiceImplTest {

    private final OffsetDateTime DATE = OffsetDateTime.now();
    private final Long attendeesNumberMock = new Random().nextLong();
    private final Long rankingMock = new Random().nextLong();
    private static final String EXISTING_FISCAL_CODE = "existing-fiscalCode";
    private static final String NOT_EXISTING_FISCAL_CODE = "not-existing-fiscalCode";

    @MockBean
    private CitizenDAO citizenDAOMock;
    @MockBean
    private CitizenRankingDAO citizenRankingDAOMock;
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

        Mockito.when(citizenRankingDAOMock.findById(Mockito.eq(new CitizenRankingId(EXISTING_FISCAL_CODE, 0L))))
                .thenAnswer((Answer<Optional<CitizenRanking>>)
                        invocation -> {
                            CitizenRanking citizenRanking = new CitizenRanking();
                            citizenRanking.setAwardPeriodId(0L);
                            citizenRanking.setFiscalCode(EXISTING_FISCAL_CODE);
                            citizenRanking.setRanking(rankingMock);
                            return Optional.of(citizenRanking);
                        });

        Mockito.when(citizenDAOMock.count()).thenAnswer((Answer<Long>)
                invocation -> attendeesNumberMock);

        Mockito.when(citizenRankingDAOMock.findById(Mockito.eq(new CitizenRankingId("wrongFiscalCode", 0L))))
                .thenAnswer((Answer<CitizenRanking>)
                        invocation -> null);

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
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizen.setFiscalCode(EXISTING_FISCAL_CODE);
        citizenService.patch(EXISTING_FISCAL_CODE, citizen);
        citizen.setUpdateUser(EXISTING_FISCAL_CODE);

        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq(EXISTING_FISCAL_CODE));
        BDDMockito.verify(citizenDAOMock).save(Mockito.eq(citizen));
        BDDMockito.verify(checkIbanRestConnectorMock).checkIban(citizen.getPayoffInstr(),citizen.getFiscalCode());
    }

    @Test(expected = CitizenNotFoundException.class)
    public void patch_KO() {
        Citizen citizen = new Citizen();
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizenService.patch(NOT_EXISTING_FISCAL_CODE, citizen);

        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq(NOT_EXISTING_FISCAL_CODE));
        BDDMockito.verifyNoMoreInteractions(citizenDAOMock);
    }


    @Test
    public void findRanking() {
        CitizenRanking citizenRanking = citizenService.findRanking(EXISTING_FISCAL_CODE, 0L);
        Assert.assertNotNull(citizenRanking);
        Assert.assertEquals(rankingMock, citizenRanking.getRanking());
        BDDMockito.verify(citizenRankingDAOMock, Mockito.atLeastOnce())
                .findById(Mockito.eq(new CitizenRankingId(EXISTING_FISCAL_CODE, 0L)));
    }

    @Test
    public void calculateAttendeesNumber() {
        Long attendeesNumber = citizenService.calculateAttendeesNumber();

        Assert.assertEquals(attendeesNumberMock, attendeesNumber);
    }


}