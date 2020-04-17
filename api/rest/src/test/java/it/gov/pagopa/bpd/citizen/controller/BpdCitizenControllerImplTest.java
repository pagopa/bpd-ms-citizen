package it.gov.pagopa.bpd.citizen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.config.ArchConfiguration;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.factory.CitizenFactory;
import it.gov.pagopa.bpd.citizen.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenDAOService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BpdCitizenControllerImpl.class)
@AutoConfigureMockMvc(secure = false)
@EnableWebMvc
public class BpdCitizenControllerImplTest {

    @Autowired
    protected MockMvc mvc;
    protected ObjectMapper objectMapper = new ArchConfiguration().objectMapper();
    @MockBean
    private CitizenDAOService citizenDAOServiceMock;
    @SpyBean
    private CitizenResourceAssembler citizenResourceAssemblerMock;
    @SpyBean
    private CitizenFactory citizenFactoryMock;

    @PostConstruct
    public void configureTest() {

        Citizen citizen = new Citizen();
        Optional<Citizen> optional = Optional.of(citizen);
        citizen.setFiscalCode("fiscalCode");

        BDDMockito.doReturn(Optional.of(citizen)).when(citizenDAOServiceMock).find(Mockito.eq("fiscalCode"));

        BDDMockito.doReturn(new Citizen()).when(citizenDAOServiceMock).update(Mockito.eq("fiscalCode"), Mockito.eq(citizen));

        BDDMockito.doNothing().when(citizenDAOServiceMock).delete(Mockito.eq("fiscalCode"));

    }


    @Test
    public void find() throws Exception {
        MvcResult result = (MvcResult) mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenResource pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenDAOServiceMock).find(Mockito.eq("fiscalCode"));
        BDDMockito.verify(citizenResourceAssemblerMock).toResource(Mockito.any(Citizen.class));
    }

    @Test
    public void update() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        MvcResult result = (MvcResult) mvc.perform(MockMvcRequestBuilders.put("/bpd/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenResource pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenDAOServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());
        BDDMockito.verify(citizenFactoryMock).createModel(Mockito.eq(citizen));
        BDDMockito.verify(citizenResourceAssemblerMock).toResource(Mockito.any(Citizen.class));
    }

    @Test
    public void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/bpd/citizens/fiscalCode"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        BDDMockito.verify(citizenDAOServiceMock).delete(Mockito.any());
    }

    @Test
    public void updateTCOK() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        MvcResult result = (MvcResult) mvc.perform(MockMvcRequestBuilders.put("/bpd/enrollment/io/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        BDDMockito.verify(citizenDAOServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());
        BDDMockito.verify(citizenFactoryMock).createModel(Mockito.eq(citizen));
    }

    @Test(expected = ArgumentsAreDifferent.class)
    public void updateTCInvalidArguments() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        try {
            MvcResult result = (MvcResult) mvc.perform(MockMvcRequestBuilders.put("/bpd/enrollment/io/citizens/wrongFiscalCode")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(objectMapper.writeValueAsString(citizen)))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn();
        } finally {
            BDDMockito.verify(citizenDAOServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());
            BDDMockito.verify(citizenFactoryMock).createModel(Mockito.eq(citizen));
        }


    }
}