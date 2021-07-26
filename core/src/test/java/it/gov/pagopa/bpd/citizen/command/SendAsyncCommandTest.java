package it.gov.pagopa.bpd.citizen.command;

import eu.sia.meda.async.util.AsyncUtils;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenEventRecord;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import it.gov.pagopa.bpd.citizen.service.CitizenStatusUpdatePublisherService;
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
import org.mockito.Spy;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;

public class SendAsyncCommandTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    CitizenService citizenServiceMock;

    @Mock
    CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherServiceMock;

    @Spy
    AsyncUtils asyncUtils;

    @Before
    public void initTest() {
        Mockito.reset(
                citizenServiceMock,
                asyncUtils,
                citizenStatusUpdatePublisherServiceMock);
    }

    @Test
    public void test_OK_EmptyList() {

        SendAsyncEventCommand sendAsyncEventCommand = new SendAsyncEventCommandImpl(
                "NOT_EXISTING_FC",
                citizenServiceMock,
                citizenStatusUpdatePublisherServiceMock,
                asyncUtils
        );

        try {
            Boolean result = sendAsyncEventCommand.execute();
            Assert.assertTrue(result);
            BDDMockito.verify(citizenServiceMock).retrieveActiveEvents(Mockito.eq("NOT_EXISTING_FC"));
            BDDMockito.verifyZeroInteractions(citizenStatusUpdatePublisherServiceMock);
            BDDMockito.verifyNoMoreInteractions(citizenServiceMock);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void test_OK_OneElement() {

        SendAsyncEventCommand sendAsyncEventCommand = new SendAsyncEventCommandImpl(
                "EXISTING_FC",
                citizenServiceMock,
                citizenStatusUpdatePublisherServiceMock,
                asyncUtils
        );

        BDDMockito.doReturn(Collections.singletonList(getModel())).when(citizenServiceMock)
                .retrieveActiveEvents(Mockito.eq("EXISTING_FC"));

        try {
            Boolean result = sendAsyncEventCommand.execute();
            Assert.assertTrue(result);
            BDDMockito.verify(citizenServiceMock).retrieveActiveEvents(Mockito.eq("EXISTING_FC"));
            BDDMockito.verify(citizenStatusUpdatePublisherServiceMock).publishCitizenStatus(
                    Mockito.eq(getStatusUpdate()));
            BDDMockito.verify(citizenServiceMock).updateEvent(Mockito.any());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    @SneakyThrows
    @Test
    public void test_KO_OneElement() {

        SendAsyncEventCommand sendAsyncEventCommand = new SendAsyncEventCommandImpl(
                "EXISTING_FC",
                citizenServiceMock,
                citizenStatusUpdatePublisherServiceMock,
                asyncUtils
        );

        when(citizenServiceMock.retrieveActiveEvents(Mockito.eq("EXISTING_FC")))
                .thenAnswer(invocation -> {
                    throw new Exception();
                });

        expectedException.expect(Exception.class);
        sendAsyncEventCommand.execute();
        BDDMockito.verify(citizenServiceMock).retrieveActiveEvents(Mockito.eq("EXISTING_FC"));
        BDDMockito.verifyZeroInteractions(citizenStatusUpdatePublisherServiceMock);
        BDDMockito.verifyNoMoreInteractions(citizenServiceMock);

    }

    CitizenEventRecord getModel() {
        CitizenEventRecord citizenEventRecord = new CitizenEventRecord();
        citizenEventRecord.setEventTimestamp(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"));
        citizenEventRecord.setFiscalCode("EXISTING_FC");
        citizenEventRecord.setEventStatus(false);
        return citizenEventRecord;
    }

    StatusUpdate getStatusUpdate() {
        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setUpdateDateTime(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"));
        statusUpdate.setFiscalCode("EXISTING_FC");
        statusUpdate.setEnabled(false);
        statusUpdate.setApplyTo("all");
        return statusUpdate;
    }


}
