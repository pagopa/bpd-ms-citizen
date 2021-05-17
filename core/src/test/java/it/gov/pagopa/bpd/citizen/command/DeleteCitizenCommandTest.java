package it.gov.pagopa.bpd.citizen.command;

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

import java.time.OffsetDateTime;

public class DeleteCitizenCommandTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    CitizenService citizenServiceMock;
    @Mock
    CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherServiceMock;


    @Before
    public void initTest() {
        Mockito.reset(
                citizenServiceMock,
                citizenStatusUpdatePublisherServiceMock);
    }

    @Test
    public void test_BDPActive() {

        StatusUpdate statusUpdate = getRequestObject();

        DeleteCitizenCommand deleteCitizenCommand = new DeleteCitizenCommandImpl(
                "fiscalCode",
                citizenServiceMock,
                citizenStatusUpdatePublisherServiceMock
        );


        try {

            BDDMockito.doNothing().when(citizenServiceMock)
                    .delete("fiscalCode");
            BDDMockito.doNothing().when(citizenStatusUpdatePublisherServiceMock)
                    .publishCitizenStatus(Mockito.eq(statusUpdate));

            Boolean isOk = deleteCitizenCommand.execute();

            Assert.assertTrue(isOk);
            BDDMockito.verify(citizenServiceMock, Mockito.atLeastOnce())
                    .delete("fiscalCode");
            BDDMockito.verify(citizenStatusUpdatePublisherServiceMock, Mockito.atLeastOnce())
                    .publishCitizenStatus(Mockito.any());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    protected DeleteCitizenCommand buildCommandInstance(String fiscalCode) {
        return new DeleteCitizenCommandImpl(
                fiscalCode,
                citizenServiceMock,
                citizenStatusUpdatePublisherServiceMock

        );
    }

    protected StatusUpdate getRequestObject() {
        return StatusUpdate.builder()
                .fiscalCode("fiscalCode")
                .enabled(false)
                .updateDateTime(OffsetDateTime.now())
                .build();
    }

    @SneakyThrows
    @Test
    public void testExecute_KO_Validation() {

        DeleteCitizenCommand deleteCitizenCommand = buildCommandInstance("fiscalCode");

        BDDMockito.doThrow(new RuntimeException("Some Exception")).when(citizenServiceMock)
                .delete(Mockito.eq("fiscalCode"));

        expectedException.expect(Exception.class);
        deleteCitizenCommand.execute();

        BDDMockito.verifyZeroInteractions(citizenServiceMock);
        BDDMockito.verifyZeroInteractions(citizenStatusUpdatePublisherServiceMock);

    }

    @Test
    public void testExecute_KO_Null() {

        DeleteCitizenCommand deleteCitizenCommand = buildCommandInstance(null);

        try {

            expectedException.expect(AssertionError.class);
            deleteCitizenCommand.execute();
            BDDMockito.verifyZeroInteractions(citizenServiceMock);
            BDDMockito.verifyZeroInteractions(deleteCitizenCommand);
            BDDMockito.verifyZeroInteractions();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

}
