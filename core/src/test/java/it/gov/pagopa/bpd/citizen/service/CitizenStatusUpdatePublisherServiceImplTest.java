package it.gov.pagopa.bpd.citizen.service;

import eu.sia.meda.BaseTest;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.citizen.publisher.CitizenStatusPublisherConnector;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.service.transformer.HeaderAwareRequestTransformer;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.OffsetDateTime;

import static org.mockito.MockitoAnnotations.initMocks;

public class CitizenStatusUpdatePublisherServiceImplTest extends BaseTest {

    @Mock
    private CitizenStatusPublisherConnector citizenStatusPublisherConnector;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherService;

    @SpyBean
    private HeaderAwareRequestTransformer<StatusUpdate> simpleEventRequestTransformerSpy;

    @SpyBean
    private SimpleEventResponseTransformer simpleEventResponseTransformerSpy;

    @Before
    public void initTest() {
        Mockito.reset(citizenStatusPublisherConnector);
        citizenStatusUpdatePublisherService =
                new CitizenStatusUpdatePublisherServiceImpl(
                        citizenStatusPublisherConnector,
                        simpleEventRequestTransformerSpy,
                        simpleEventResponseTransformerSpy);
    }

    @Test
    public void testSave_Ok() {

        try {

            BDDMockito.doReturn(true)
                    .when(citizenStatusPublisherConnector)
                    .doCall(Mockito.eq(getSaveModel()),Mockito.any(),Mockito.any());

            citizenStatusUpdatePublisherService.publishCitizenStatus(getSaveModel());

            BDDMockito.verify(citizenStatusPublisherConnector, Mockito.atLeastOnce())
                    .doCall(Mockito.any(),Mockito.any(),Mockito.any(), Mockito.any());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @SneakyThrows
    @Test
    public void testSave_KO_Connector() {

        BDDMockito.doAnswer(invocationOnMock -> {
            throw new Exception();
        }).when(citizenStatusPublisherConnector)
                .doCall(Mockito.any(),Mockito.any(),Mockito.any(), Mockito.any());

        expectedException.expect(Exception.class);
        citizenStatusUpdatePublisherService.publishCitizenStatus(null);

        BDDMockito.verify(citizenStatusPublisherConnector, Mockito.atLeastOnce())
                .doCall(Mockito.any(),Mockito.any(),Mockito.any(), Mockito.any());

    }

    protected StatusUpdate getSaveModel() {
        return StatusUpdate.builder()
                .enabled(false)
                .fiscalCode("FISCAL_CODE")
                .updateDateTime(OffsetDateTime.now())
                .build();
    }

}