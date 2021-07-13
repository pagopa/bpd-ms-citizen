package it.gov.pagopa.bpd.citizen.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.DummyConfiguration;
import eu.sia.meda.config.ArchConfiguration;
import eu.sia.meda.error.config.LocalErrorConfig;
import eu.sia.meda.error.handler.MedaExceptionHandler;
import eu.sia.meda.error.service.impl.LocalErrorManagerServiceImpl;
import it.gov.pagopa.bpd.citizen.assembler.CitizenCashbackResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingMilestoneResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenRankingResourceAssembler;
import it.gov.pagopa.bpd.citizen.assembler.CitizenResourceAssembler;
import it.gov.pagopa.bpd.citizen.command.DeleteCitizenCommand;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.CitizenTransactionMilestoneConverter;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRanking;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenRankingId;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.CashbackResource;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.resource.GetTotalCashbackResource;
import it.gov.pagopa.bpd.citizen.factory.CitizenFactory;
import it.gov.pagopa.bpd.citizen.factory.CitizenPatchFactory;
import it.gov.pagopa.bpd.citizen.model.*;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = {BpdCitizenControllerImpl.class}, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = {
        BpdCitizenControllerImpl.class,
        DummyConfiguration.class,
        MedaExceptionHandler.class,
        LocalErrorManagerServiceImpl.class
})
@Import(LocalErrorConfig.class)
@TestPropertySource(properties = {
        "error-manager.enabled=true",
        "spring.application.name=bpd-ms-citizen-api-rest"
})
public class BpdCitizenControllerImplTest {

    @Autowired
    protected MockMvc mvc;
    protected ObjectMapper objectMapper = new ArchConfiguration().objectMapper();
    @MockBean
    private CitizenService citizenServiceSpy;
    @MockBean
    DeleteCitizenCommand deleteCitizenCommandMock;
    @SpyBean
    private CitizenResourceAssembler citizenResourceAssemblerSpy;
    @SpyBean
    private CitizenCashbackResourceAssembler citizenCashbackResourceAssemblerSpy;
    @SpyBean
    private CitizenFactory citizenFactorySpy;
    @SpyBean
    private CitizenPatchFactory citizenPatchFactorySpy;
    @SpyBean
    private CitizenRankingResourceAssembler citizenRankingResourceAssemblerSpy;
    @SpyBean
    private CitizenRankingMilestoneResourceAssembler citizenRankingMilestoneResourceAssemblerSpy;

    private final CitizenTransactionConverter citizenTransactionConverter = BDDMockito.mock(CitizenTransactionConverter.class);
    private final CitizenTransactionMilestoneConverter citizenTransactionMilestoneConverter = BDDMockito.mock(CitizenTransactionMilestoneConverter.class);
    private final List<CitizenTransactionConverter> citizenRanking = Collections.singletonList(citizenTransactionConverter);
    private final List<CitizenTransactionMilestoneConverter> citizenRankingMilestone = Collections.singletonList(citizenTransactionMilestoneConverter);

    @SneakyThrows
    @PostConstruct
    public void configureTest() {

        Citizen citizen = new Citizen();
        citizen.setFiscalCode("AAAAAA00A00A000A");

        BDDMockito.doReturn(citizen).when(citizenServiceSpy).find(Mockito.eq("AAAAAA00A00A000A"));
        Citizen citizenPatched = new Citizen();
        citizenPatched.setPayoffInstr("Test");
        citizenPatched.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizenPatched.setUpdateUser("AAAAAA00A00A000A");

        Citizen citizenPatch = new Citizen();
        citizenPatch.setPayoffInstr("Test");
        citizenPatch.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);

        Mockito.when(citizenTransactionConverter.getFiscalCode()).thenReturn("AAAAAA00A00A000A");
        Mockito.when(citizenTransactionConverter.getRanking()).thenReturn(1L);
        Mockito.when(citizenTransactionConverter.getTotalParticipants()).thenReturn(100L);
        Mockito.when(citizenTransactionConverter.getMaxTrxNumber()).thenReturn(15L);
        Mockito.when(citizenTransactionConverter.getMinTrxNumber()).thenReturn(2L);
        Mockito.when(citizenTransactionConverter.getTrxNumber()).thenReturn(5L);
        Mockito.when(citizenTransactionConverter.getAwardPeriodId()).thenReturn(1L);

        Mockito.when(citizenTransactionMilestoneConverter.getFiscalCode()).thenReturn("AAAAAA00A00A000A");
        Mockito.when(citizenTransactionMilestoneConverter.getRanking()).thenReturn(1L);
        Mockito.when(citizenTransactionMilestoneConverter.getTotalParticipants()).thenReturn(100L);
        Mockito.when(citizenTransactionMilestoneConverter.getMaxTrxNumber()).thenReturn(15L);
        Mockito.when(citizenTransactionMilestoneConverter.getMinTrxNumber()).thenReturn(2L);
        Mockito.when(citizenTransactionMilestoneConverter.getTrxNumber()).thenReturn(5L);
        Mockito.when(citizenTransactionMilestoneConverter.getAwardPeriodId()).thenReturn(1L);
        Mockito.when(citizenTransactionMilestoneConverter.getIdTrxPivot()).thenReturn("idTrxPivot");
        Mockito.when(citizenTransactionMilestoneConverter.getCashbackNorm()).thenReturn(new BigDecimal("1"));
        Mockito.when(citizenTransactionMilestoneConverter.getIdTrxMinTransactionNumber()).thenReturn("idTrxMinTransactionNumber");
        Mockito.when(citizenTransactionMilestoneConverter.getTotalCashback()).thenReturn(new BigDecimal("150"));
        Mockito.when(citizenTransactionMilestoneConverter.getMaxCashback()).thenReturn(new BigDecimal("150"));

        CitizenRanking cashback = new CitizenRanking();
        cashback.setTotalCashback(new BigDecimal(100));
        cashback.setTransactionNumber(10L);

        CitizenRankingId id = new CitizenRankingId();
        id.setFiscalCode("AAAAAA00A00A000A");
        id.setAwardPeriodId(1L);


        BDDMockito.doReturn(true).when(deleteCitizenCommandMock).execute();

        BDDMockito.doReturn(citizen).when(citizenServiceSpy).find(Mockito.eq("AAAAAA00A00A000A"));

        BDDMockito.doReturn(new Citizen()).when(citizenServiceSpy).update(Mockito.eq("AAAAAA00A00A000A"), Mockito.any());

        BDDMockito.doReturn(true).when(citizenServiceSpy).delete(Mockito.eq("AAAAAA00A00A000A"));

        BDDMockito.doReturn("OK").when(citizenServiceSpy).patch(Mockito.eq("AAAAAA00A00A000A"), Mockito.eq(citizenPatch));

        BDDMockito.doThrow(new EntityNotFoundException("Unable to find " + Citizen.class.getName() + " with id noFiscalCode"))
                .when(citizenServiceSpy).patch(Mockito.eq("noFiscalCode"), Mockito.any());

        BDDMockito.doReturn(citizenRanking).when(citizenServiceSpy).findRankingDetails(Mockito.eq("AAAAAA00A00A000A"), Mockito.anyLong());

        BDDMockito.doReturn(new GetTotalCashbackResource() {
            @Override
            public String getFiscalCode() {
                return "AAAAAA00A00A000A";
            }

            @Override
            public Long getAwardPeriodId() {
                return 1L;
            }

            @Override
            public BigDecimal getTotalCashback() {
                return new BigDecimal(100);
            }

            @Override
            public BigDecimal getMaxTotalCashback() {
                return new BigDecimal(150);
            }

            @Override
            public Long getTransactionNumber() {
                return 10L;
            }
        }).when(citizenServiceSpy).getTotalCashback(Mockito.eq(id));

        BDDMockito.when(citizenServiceSpy.findRankingMilestoneDetails(Mockito.eq("AAAAAA00A00A000A"), Mockito.anyLong())).thenReturn(citizenRankingMilestone);
    }


    @Test
    public void find() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/AAAAAA00A00A000A")
                .param("isIssuer", "false")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenUpdateResource pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenUpdateResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenResourceAssemblerSpy).toCitizenResource(Mockito.any(Citizen.class), Mockito.eq(null), Mockito.eq(false));

        result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/AAAAAA00A00A000A")
                .param("flagTechnicalAccount", "true")
                .param("isIssuer", "true")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenUpdateResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenResourceAssemblerSpy).toCitizenResource(Mockito.any(Citizen.class), Mockito.eq(true), Mockito.eq(true));

        result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/AAAAAA00A00A000A")
                .param("flagTechnicalAccount", "false")
                .param("isIssuer", "true")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenUpdateResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenResourceAssemblerSpy).toCitizenResource(Mockito.any(Citizen.class), Mockito.eq(false), Mockito.eq(true));

        result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/AAAAAA00A00A000A")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenUpdateResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenResourceAssemblerSpy).toCitizenResource(Mockito.any(Citizen.class), Mockito.eq(null), Mockito.eq(null));

        BDDMockito.verify(citizenServiceSpy, Mockito.times(4)).find(Mockito.eq("AAAAAA00A00A000A"));

    }

    @Test
    public void update() throws Exception {
        CitizenDTO citizen = new CitizenDTO();
        citizen.setTimestampTC(OffsetDateTime.now());
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/bpd/citizens/AAAAAA00A00A000A")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CitizenUpdateResource pageResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                CitizenUpdateResource.class);

        Assert.assertNotNull(pageResult);
        BDDMockito.verify(citizenServiceSpy).update(Mockito.eq("AAAAAA00A00A000A"), Mockito.any());
        BDDMockito.verify(citizenFactorySpy).createModel(Mockito.any());
        BDDMockito.verify(citizenResourceAssemblerSpy).toCitizenUpdateResource(Mockito.any(Citizen.class));
    }

    @Test
    public void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/bpd/citizens/AAAAAA00A00A000A"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        BDDMockito.verify(deleteCitizenCommandMock).execute();
    }

    @Test
    public void updatePaymentMethod() throws Exception {

        CitizenPatchDTO citizen = new CitizenPatchDTO();
        citizen.setPayoffInstr("IT12A1234512345123456789012");
        citizen.setPayoffInstrType(Citizen.PayoffInstrumentType.IBAN);
        citizen.setAccountHolderCF("DTUMTO13B14I814Z");
        citizen.setAccountHolderName("accountHolderName");
        citizen.setAccountHolderSurname("accountHolderSurname");
        citizen.setTechnicalAccountHolder("technicalAccountHolder");
        citizen.setIssuerCardId("issuerCardId");

        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/DTUMTO13B14I814Z")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(citizen)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        BDDMockito.verify(citizenPatchFactorySpy).createModel(Mockito.eq(citizen));
    }


    @Test
    public void updatePaymentMethodKoValidation() throws Exception {

        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/noFiscalCode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new CitizenPatchDTO())))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        BDDMockito.verifyZeroInteractions(citizenServiceSpy);
        BDDMockito.verifyZeroInteractions(citizenPatchFactorySpy);
        BDDMockito.verifyZeroInteractions(citizenResourceAssemblerSpy);
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
//        mvc.perform(MockMvcRequestBuilders.patch("/bpd/citizens/AAAAAA00A00A000A")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
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
                .get("/bpd/citizens/AAAAAA00A00A000A/ranking")
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        List<CitizenRankingResource> citizenRankingResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<CitizenRankingResource>>() {
                });

        Assert.assertNotNull(citizenRankingResult);
        BDDMockito.verify(citizenServiceSpy).findRankingDetails(Mockito.eq("AAAAAA00A00A000A"), Mockito.anyLong());
    }

    @Test
    public void findRankingMilestone() throws Exception {
        Long awardPeriodId = 1L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/AAAAAA00A00A000A/ranking/milestone")
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .param("fiscalCode", "AAAAAA00A00A000A")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        List<CitizenRankingMilestoneResource> citizenRankingResult = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<CitizenRankingMilestoneResource>>() {
                });

        Assert.assertNotNull(citizenRankingResult);
        BDDMockito.verify(citizenServiceSpy).findRankingMilestoneDetails(Mockito.eq("AAAAAA00A00A000A"), Mockito.anyLong());
        BDDMockito.verify(citizenRankingMilestoneResourceAssemblerSpy).toResource(Mockito.eq(citizenRankingMilestone));
    }

    @Test
    public void getTotalCashback() throws Exception {
        CitizenRankingId id = new CitizenRankingId();
        Long awardPeriodId = 1L;
        String fiscalCode = "AAAAAA00A00A000A";
        id.setFiscalCode(fiscalCode);
        id.setAwardPeriodId(awardPeriodId);
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/bpd/citizens/total-cashback")
                .param("fiscalCode", fiscalCode)
                .param("awardPeriodId", String.valueOf(awardPeriodId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();
        CashbackResource resource = objectMapper.readValue(result.getResponse().getContentAsString(),
                CashbackResource.class);


        Assert.assertNotNull(resource);
        Assert.assertEquals(resource.getTotalCashback(), new BigDecimal(100));
        BDDMockito.verify(citizenServiceSpy).getTotalCashback(Mockito.eq(id));
    }

}