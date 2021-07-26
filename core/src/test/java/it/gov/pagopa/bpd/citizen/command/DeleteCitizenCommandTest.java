package it.gov.pagopa.bpd.citizen.command;

import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
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
import org.springframework.beans.factory.BeanFactory;

import java.time.OffsetDateTime;

public class DeleteCitizenCommandTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    CitizenService citizenServiceMock;
    @Mock
    BeanFactory beanFactory;
    @Mock
    SendAsyncEventCommand sendAsyncEventCommand;


    @Before
    public void initTest() {
        Mockito.reset(
                citizenServiceMock,
                beanFactory
        );
    }

    @Test
    public void test_OK_Active() {

        StatusUpdate statusUpdate = getRequestObject();

        DeleteCitizenCommand deleteCitizenCommand = new DeleteCitizenCommandImpl(
                "fiscalCode",
                citizenServiceMock,
                beanFactory
        );

        try {

            BDDMockito.doReturn(true).when(citizenServiceMock)
                    .delete("fiscalCode");
            BDDMockito.doReturn(sendAsyncEventCommand).when(beanFactory)
                    .getBean(Mockito.eq(SendAsyncEventCommand.class), Mockito.any());
            BDDMockito.doReturn(true).when(sendAsyncEventCommand)
                    .execute();

            Boolean isOk = deleteCitizenCommand.execute();

            Assert.assertTrue(isOk);
            BDDMockito.verify(citizenServiceMock, Mockito.atLeastOnce())
                    .delete("fiscalCode");
            BDDMockito.verify(sendAsyncEventCommand, Mockito.atLeastOnce()).execute();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

    protected DeleteCitizenCommand buildCommandInstance(String fiscalCode) {
        return new DeleteCitizenCommandImpl(
                fiscalCode,
                citizenServiceMock,
                beanFactory

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
        BDDMockito.verifyZeroInteractions(sendAsyncEventCommand);

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
