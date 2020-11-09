package it.gov.pagopa.bpd.citizen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.config.ArchConfiguration;
import it.gov.pagopa.bpd.citizen.assembler.CitizenCashbackResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.CashbackResource;
import it.gov.pagopa.bpd.citizen.factory.CitizenFactory;
import it.gov.pagopa.bpd.citizen.factory.CitizenPatchFactory;
import it.gov.pagopa.bpd.citizen.model.CitizenDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenPatchDTO;
import it.gov.pagopa.bpd.citizen.model.CitizenRankingResource;
import it.gov.pagopa.bpd.citizen.model.CitizenResource;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BpdCitizenControllerImpl.class)
@AutoConfigureMockMvc(secure = false)
@EnableWebMvc
public class BpdCitizenControllerImplTest {


    @Autowired
    protected MockMvc mvc;
    protected ObjectMapper objectMapper = new ArchConfiguration().objectMapper();
    @MockBean
    private CitizenService citizenServiceMock;
    @SpyBean
    private CitizenResourceAssembler citizenResourceAssemblerMock;
    @SpyBean
    private CitizenCashbackResourceAssembler citizenCashbackResourceAssemblerMock;
    @SpyBean
    private CitizenFactory citizenFactoryMock;
    @SpyBean
    private CitizenPatchFactory citizenPatchFactoryMock;
    @SpyBean
    private CitizenRankingResourceAssembler citizenRankingResourceAssemblerMock;

    @PostConstruct
    public void configureTest() {

        Citizen citizen = new Citizen();
        citizen.setFiscalCode("fiscalCode");

        BDDMockito.doReturn(citizen).when(citizenServiceMock).find(Mockito.eq("fiscalCode"));
        Citizen citizenPatched = new Citizen();
        citizenPatched.setPayoffInstr("Test");
        citizenPatched.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizenPatched.setUpdateUser("fiscalCode");

        Citizen citizenPatch = new Citizen();
        citizenPatch.setPayoffInstr("Test");
        citizenPatch.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);


        List<CitizenTransactionConverter> citizenRanking = new ArrayList<>();
        CitizenTransactionConverter item = new CitizenTransactionConverter() {
            @Override
            public Long getRanking() {
                return 1L;
            }

            @Override
            public Long getTotalParticipants() {
                return 100L;
            }

            @Override
            public Long getMaxTrxNumber() {
                return 15L;
            }

            @Override
            public Long getMinTrxNumber() {
                return 2L;
            }

            @Override
            public Long getTrxNumber() {
                return 5L;
            }

            @Override
            public Long getAwardPeriodId() {
                return 1L;
            }
        };
        citizenRanking.add(item);

        CitizenRankingResource citizenRankingResource = new CitizenRankingResource();
        citizenRankingResource.setRanking(2L);
        citizenRankingResource.setTotalParticipants(100L);
        citizenRankingResource.setMaxTransactionNumber(10L);
        citizenRankingResource.setMinTransactionNumber(1L);
        citizenRankingResource.setTransactionNumber(3L);
        citizenRankingResource.setAwardPeriodId(1L);

        CitizenRanking cashback = new CitizenRanking();
        cashback.setTotalCashback(new BigDecimal(100));
        cashback.setTransactionNumber(10L);

        CitizenRankingId id = new CitizenRankingId();
        id.setFiscalCode("fiscalCode");
        id.setAwardPeriodId(1L);


        BDDMockito.doReturn(citizen).when(citizenServiceMock).find(Mockito.eq("fiscalCode"));

        BDDMockito.doReturn(new Citizen()).when(citizenServiceMock).update(Mockito.eq("fiscalCode"), Mockito.any());

        BDDMockito.doNothing().when(citizenServiceMock).delete(Mockito.eq("fiscalCode"));

        BDDMockito.doReturn("OK").when(citizenServiceMock).patch(Mockito.eq("fiscalCode"), Mockito.eq(citizenPatch));

        BDDMockito.doThrow(new EntityNotFoundException("Unable to find " + Citizen.class.getName() + " with id noFiscalCode"))
                .when(citizenServiceMock).patch(Mockito.eq("noFiscalCode"), Mockito.any());

        BDDMockito.doReturn(citizenRanking).when(citizenServiceMock).findRankingDetails(Mockito.eq("fiscalCode"), Mockito.anyLong());

        BDDMockito.doReturn(cashback).when(citizenServiceMock).getTotalCashback(Mockito.eq(id));
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
        citizen.setTimestampTC(OffsetDateTime.now());
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
        BDDMockito.verify(citizenFactoryMock).createModel(Mockito.any());
        BDDMockito.verify(citizenResourceAssemblerMock).toResource(Mockito.any(Citizen.class));
    }

    @Test
    public void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/bpd/citizens/fiscalCode"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        BDDMockito.verify(citizenServiceMock).delete(Mockito.any());
    }

    @Test
    public void updatePaymentMethod() throws Exception {

        CitizenPatchDTO citizen = new CitizenPatchDTO();
        citizen.setPayoffInstr("IT12A1234512345123456789012");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizen.setAccountHolderCF("DTUMTO13I14I814Z");
        citizen.setAccountHolderName("accountHolderName");
        citizen.setAccountHolderSurname("accountHolderSurname");



        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/fiscalCode")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

     BDDMockito.verify(citizenPatchFactoryMock).createModel(Mockito.eq(citizen));
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

//    @Test
//    public void updatePaymentMethodKoIbanValidation() throws Exception {
//
//        CitizenPatchDTO citizen = new CitizenPatchDTO();
//        citizen.setPayoffInstr("IT12A123451234");
//        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
//        citizen.setAccountHolderCF("DTUMTO13I14I814Z");
//        citizen.setAccountHolderName("accountHolderName");
//        citizen.setAccountHolderSurname("accountHolderSurname");
//
//
//        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/fiscalCode")
//                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                .content(objectMapper.writeValueAsString(citizen)))
//                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
//                .andReturn();
//
//        BDDMockito.verifyZeroInteractions(citizenServiceMock);
//        BDDMockito.verifyZeroInteractions(citizenPatchFactoryMock);
//        BDDMockito.verifyZeroInteractions(citizenResourceAssemblerMock);
//    }

    @Test
    public void findRanking() throws Exception {
        Long awardPeriodId = 1L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/fiscalCode/ranking")
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        List<CitizenRankingResource> citizenRankingResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                List.class);

        Assert.assertNotNull(citizenRankingResult);
        BDDMockito.verify(citizenServiceMock).findRankingDetails(Mockito.eq("fiscalCode"), Mockito.anyLong());
    }

    @Test
    public void getTotalCashback() throws Exception {
        CitizenRankingId id = new CitizenRankingId();
        Long awardPeriodId = 1L;
        String fiscalCode = "fiscalCode";
        id.setFiscalCode(fiscalCode);
        id.setAwardPeriodId(awardPeriodId);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/total-cashback")
                .param("fiscalCode", fiscalCode)
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CashbackResource resource = objectMapper.readValue(result.getResponse().getContentAsString(),
                CashbackResource.class);


        Assert.assertNotNull(resource);
        Assert.assertEquals(resource.getTotalCashback(), new BigDecimal(100));
        BDDMockito.verify(citizenServiceMock).getTotalCashback(Mockito.eq(id));
    }

}