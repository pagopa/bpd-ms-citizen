package it.gov.pagopa.bpd.citizen.service;

import it.gov.pagopa.bpd.citizen.dao.CitizenDAO;
import it.gov.pagopa.bpd.citizen.dao.FileStorageDAO;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.citizen.dao.model.FileStorage;
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

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CitizenServiceImpl.class)
public class CitizenServiceImplTest {

    private final OffsetDateTime DATE = OffsetDateTime.now();

    @MockBean
    private CitizenDAO citizenDAOMock;
    @MockBean
    private FileStorageDAO fileStorageDAOMock;
    @Autowired
    private CitizenService citizenService;


    @Before
    public void initTest() {
        Mockito.reset(citizenDAOMock);
        Mockito.reset(fileStorageDAOMock);

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

        FileStorage fileStorage = new FileStorage();
        fileStorage.setFile("prova".getBytes());
        fileStorage.setType("prova");
        Mockito.when(fileStorageDAOMock.getFile(Mockito.eq(DATE), Mockito.eq(fileStorage.getType()))).thenAnswer((Answer<FileStorage>)
                invocation -> fileStorage);
    }


    @Test
    public void find() {
        Optional<Citizen> citizen = citizenService.find("test");

        Assert.assertNotNull(citizen.orElse(null));
        BDDMockito.verify(citizenDAOMock).findById(Mockito.eq("test"));
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
        citizenService.delete("test");

        BDDMockito.verify(citizenDAOMock).save(Mockito.any(Citizen.class));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void patch() {
        Citizen citizen = new Citizen();
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizenService.patch("fiscalCode", citizen);
        citizen.setUpdateUser("fiscalCode");

        BDDMockito.verify(citizenDAOMock).getOne(Mockito.eq("fiscalCode"));
        BDDMockito.verify(citizenDAOMock).save(Mockito.eq(citizen));
    }

    @Test
    public void patch_KO() {
        BDDMockito.when(citizenDAOMock.getOne(Mockito.eq("NoFiscalCode"))).
                thenThrow(new EntityNotFoundException(
                        "Unable to find " + Citizen.class.getName() + " with id NoFiscalCode"));

        Citizen citizen = new Citizen();
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        exceptionRule.expect(EntityNotFoundException.class);
        exceptionRule.expectMessage("Unable to find " + Citizen.class.getName() + " with id NoFiscalCode");
        citizenService.patch("NoFiscalCode", citizen);

        BDDMockito.verify(citizenDAOMock).getOne(Mockito.eq("NoFiscalCode"));
        BDDMockito.verifyNoMoreInteractions(citizenDAOMock);
    }

    @Test
    public void getPdf() {
        FileStorage file = citizenService.getFile(DATE, "prova");

        Assert.assertNotNull(file);
        BDDMockito.verify(fileStorageDAOMock).getFile(DATE, "prova");
    }

    @Test(expected = NullPointerException.class)
    public void getPdf_KO() {
        FileStorage file = citizenService.getFile(OffsetDateTime.parse("1970-01-01T00:00:00.000Z"), "prova");

        Assert.assertNull(file);
        BDDMockito.verify(fileStorageDAOMock).getFile(DATE, "prova");
    }
}