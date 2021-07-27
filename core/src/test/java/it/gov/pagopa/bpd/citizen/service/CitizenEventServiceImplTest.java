package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.command.SendAsyncEventCommand;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenEventRecord;
import it.gov.pagopa.bpd.common.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;

public class CitizenEventServiceImplTest extends BaseTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    BeanFactory beanFactory;

    @Mock
    SendAsyncEventCommand sendAsyncEventCommand;

    @Before
    public void initTest() {
        Mockito.reset(beanFactory, sendAsyncEventCommand);
        BDDMockito.doReturn(sendAsyncEventCommand).when(beanFactory).getBean(SendAsyncEventCommand.class);
    }

    @Test
    public void test_OK() {
        try {
            CitizenEventService citizenEventService = new CitizenEventServiceImpl(new ObjectProvider<BeanFactory>() {
                @Override
                public BeanFactory getObject(Object... objects) throws BeansException {
                    return beanFactory;
                }

                @Override
                public BeanFactory getIfAvailable() throws BeansException {
                    return beanFactory;
                }

                @Override
                public BeanFactory getIfUnique() throws BeansException {
                    return beanFactory;
                }

                @Override
                public BeanFactory getObject() throws BeansException {
                    return beanFactory;
                }
            });
            citizenEventService.retrySendingEvents();
            BDDMockito.verify(sendAsyncEventCommand).execute();
        } catch (Exception e) {
            Assert.fail();
        }

    }


}
