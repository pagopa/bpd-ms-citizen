package it.gov.pagopa.bpd.citizen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.config.ArchConfiguration;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.dao.model.Citizen;
import it.gov.pagopa.bpd.citizen.dao.model.FileStorage;
import it.gov.pagopa.bpd.citizen.factory.CitizenFactory;
import it.gov.pagopa.bpd.citizen.factory.CitizenPatchFactory;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BpdCitizenControllerImpl.class)
@AutoConfigureMockMvc(secure = false)
@EnableWebMvc
public class BpdCitizenControllerImplTest {

    private final OffsetDateTime DATE = OffsetDateTime.now();

    @Autowired
    protected MockMvc mvc;
    protected ObjectMapper objectMapper = new ArchConfiguration().objectMapper();
    @MockBean
    private CitizenService citizenServiceMock;
    @SpyBean
    private CitizenResourceAssembler citizenResourceAssemblerMock;
    @SpyBean
    private CitizenFactory citizenFactoryMock;
    @SpyBean
    private CitizenPatchFactory citizenPatchFactoryMock;

    @PostConstruct
    public void configureTest() {

        Citizen citizen = new Citizen();
        citizen.setFiscalCode("fiscalCode");

        BDDMockito.doReturn(Optional.of(citizen)).when(citizenServiceMock).find(Mockito.eq("fiscalCode"));
        Citizen citizenPatched = new Citizen();
        citizenPatched.setPayoffInstr("Test");
        citizenPatched.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizenPatched.setUpdateUser("fiscalCode");

        Citizen citizenPatch = new Citizen();
        citizenPatch.setPayoffInstr("Test");
        citizenPatch.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);

        BDDMockito.doReturn(Optional.of(citizen)).when(citizenServiceMock).find(Mockito.eq("fiscalCode"));

        BDDMockito.doReturn(new Citizen()).when(citizenServiceMock).update(Mockito.eq("fiscalCode"), Mockito.eq(citizen));

        BDDMockito.doNothing().when(citizenServiceMock).delete(Mockito.eq("fiscalCode"));

        BDDMockito.doReturn(citizenPatched).when(citizenServiceMock).patch(Mockito.eq("fiscalCode"), Mockito.eq(citizenPatch));

        BDDMockito.doThrow(new EntityNotFoundException("Unable to find " + Citizen.class.getName() + " with id noFiscalCode"))
                .when(citizenServiceMock).patch(Mockito.eq("noFiscalCode"), Mockito.any());

        FileStorage fileStorage = new FileStorage();
        fileStorage.setFile("prova".getBytes());
        Mockito.when(citizenServiceMock.getFile(Mockito.any(OffsetDateTime.class), Mockito.eq("TC"))).thenAnswer((Answer<FileStorage>)
                invocation -> fileStorage);
    }


    @Test
    public void find() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenResource pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenServiceMock).find(Mockito.eq("fiscalCode"));
        BDDMockito.verify(citizenResourceAssemblerMock).toResource(Mockito.any(Citizen.class));
    }

    @Test
    public void update() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/bpd/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenResource pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());
        BDDMockito.verify(citizenFactoryMock).createModel(Mockito.eq(citizen));
        BDDMockito.verify(citizenResourceAssemblerMock).toResource(Mockito.any(Citizen.class));
    }

    @Test
    public void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/bpd/citizens/fiscalCode"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        BDDMockito.verify(citizenServiceMock).delete(Mockito.any());
    }

    @Test
    public void updateTC() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        mvc.perform(MockMvcRequestBuilders.put("/bpd/enrollment/io/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        BDDMockito.verify(citizenServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());
        BDDMockito.verify(citizenFactoryMock).createModel(Mockito.eq(citizen));
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void updateTCInvalidArguments() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        try {
            mvc.perform(MockMvcRequestBuilders.put("/bpd/enrollment/io/citizens/wrongFiscalCode")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(objectMapper.writeValueAsString(citizen)))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn();
        } finally {
            BDDMockito.verify(citizenServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());
            BDDMockito.verify(citizenFactoryMock).createModel(Mockito.eq(citizen));
        }
    }

    @Test
    public void updatePaymentMethod() throws Exception {

        CitizenPatchDTO citizen = new CitizenPatchDTO();
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);

        Citizen expCitizen = new Citizen();
        expCitizen.setPayoffInstr("Test");
        expCitizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);

        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        BDDMockito.verify(citizenServiceMock).patch(Mockito.eq("fiscalCode"), Mockito.eq(expCitizen));
        BDDMockito.verify(citizenPatchFactoryMock).createModel(Mockito.eq(citizen));
        expCitizen.setUpdateUser("fiscalCode");
        BDDMockito.verify(citizenResourceAssemblerMock).toResource(Mockito.eq(expCitizen));
    }

    @Test
    public void updatePaymentMethodKO() throws Exception {

        CitizenPatchDTO citizen = new CitizenPatchDTO();
        citizen.setPayoffInstr("Test");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);

        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/noFiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        BDDMockito.verify(citizenServiceMock).patch(Mockito.eq("noFiscalCode"), Mockito.any());
        BDDMockito.verify(citizenPatchFactoryMock).createModel(Mockito.any());
        BDDMockito.verifyZeroInteractions(citizenResourceAssemblerMock);
    }

    @Test
    public void updatePaymentMethodKoValidation() throws Exception {

        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/noFiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(new CitizenPatchDTO())))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        BDDMockito.verifyZeroInteractions(citizenServiceMock);
        BDDMockito.verifyZeroInteractions(citizenPatchFactoryMock);
        BDDMockito.verifyZeroInteractions(citizenResourceAssemblerMock);
    }

    @Test
    public void tcReport() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/bpd/citizens/tc")
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .accept(MediaType.APPLICATION_PDF_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        byte[] pageResult = result.getResponse().getContentAsByteArray();

        Assert.assertNotNull(pageResult);
        Assert.assertEquals(pageResult, pageResult);
        BDDMockito.verify(citizenServiceMock).getFile(Mockito.any(OffsetDateTime.class), Mockito.eq("TC"));
    }

}