package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.service.service.CitizenDAO;
import it.gov.pagopa.bpd.citizen.service.service.model.entity.Citizen;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CitizenDAOServiceImpl.class)
public class CitizenDAOServiceImplTest {

    @MockBean
    private CitizenDAO citizenDAOMock;
    @Autowired
    private CitizenDAOService citizenDAOService;

    @Before
    public void initTest() {
        Mockito.reset(citizenDAOMock);

        Mockito.when(citizenDAOMock.findById(Mockito.any())).thenAnswer((Answer<Optional<Citizen>>)
                invocation -> {
                    Citizen citizen = new Citizen();
                    return Optional.of(citizen);
                });

        Mockito.when(citizenDAOMock.getOne(Mockito.anyString())).thenAnswer((Answer<Citizen>)
                invocation -> {
                    Citizen citizen = new Citizen();
                    return citizen;
                });

        Citizen citizen = new Citizen();
        BDDMockito.when(citizenDAOMock.save(Mockito.eq(citizen))).thenAnswer((Answer<Citizen>)
                invocation -> citizen);
    }


    @Test
    public void find() {
        Optional<Citizen> citizen = citizenDAOService.find("test");
        Assert.assertNotNull(citizen.orElse(null));
        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq("test"));
    }

    @Test
    public void update() {
        Citizen citizen = new Citizen();
        citizenDAOService.update("test", citizen);
        Assert.assertNotNull(citizen);
        BDDMockito.verify(citizenDAOMock).save(Mockito.eq(citizen));
    }

    @Test
    public void delete() {
        citizenDAOService.delete("test");
        BDDMockito.verify(citizenDAOMock).save(Mockito.any(Citizen.class));
    }
}