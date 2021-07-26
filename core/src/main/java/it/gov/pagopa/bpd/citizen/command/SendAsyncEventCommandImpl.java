package it.gov.pagopa.bpd.citizen.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenEventRecord;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import it.gov.pagopa.bpd.citizen.service.CitizenStatusUpdatePublisherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class SendAsyncEventCommandImpl extends BaseCommand<Boolean> implements SendAsyncEventCommand {

    private CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherService;
    private CitizenService citizenService;

    private String fiscalCode;

    public SendAsyncEventCommandImpl(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public SendAsyncEventCommandImpl(
            String fiscalCode,
            CitizenService citizenService,
            CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherService,
            AsyncUtils asyncUtils) {
        this.fiscalCode = fiscalCode;
        this.citizenService = citizenService;
        this.citizenStatusUpdatePublisherService = citizenStatusUpdatePublisherService;
        super.asyncUtils = asyncUtils;
    }

    @SneakyThrows
    @Override
    public Boolean doExecute() {

        List<CitizenEventRecord> citizenEventRecordList = citizenService.retrieveActiveEvents(fiscalCode);

        for (CitizenEventRecord citizenEventRecord : citizenEventRecordList) {
            super.callAsyncService(() -> {
                citizenStatusUpdatePublisherService.publishCitizenStatus(
                        StatusUpdate.builder()
                                .fiscalCode(citizenEventRecord.getFiscalCode())
                                .updateDateTime(citizenEventRecord.getEventTimestamp())
                                .enabled(citizenEventRecord.getEventStatus())
                                .applyTo("all")
                                .build()
                );
                citizenEventRecord.setSentTimestamp(OffsetDateTime.now());
                citizenService.updateEvent(citizenEventRecord);
                return true;
            });
        }

        return true;
    }

    @Autowired
    public void setCitizenStatusUpdatePublisherService(
            CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherService) {
        this.citizenStatusUpdatePublisherService = citizenStatusUpdatePublisherService;
    }

    @Autowired
    public void setCitizenService(CitizenService citizenService) {
        this.citizenService = citizenService;
    }


}
