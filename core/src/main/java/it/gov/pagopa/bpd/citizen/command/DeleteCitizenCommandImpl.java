package it.gov.pagopa.bpd.citizen.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen.publisher.model.StatusUpdate;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import it.gov.pagopa.bpd.citizen.service.CitizenStatusUpdatePublisherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * Base implementation of the SaveTransactionCommandInterface, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class DeleteCitizenCommandImpl extends BaseCommand<Boolean> implements DeleteCitizenCommand {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherService;
    private CitizenService citizenService;

    private String fiscalCode;


    public DeleteCitizenCommandImpl(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public DeleteCitizenCommandImpl(
            String fiscalCode,
            CitizenService citizenService,
            CitizenStatusUpdatePublisherService citizenStatusUpdatePublisherService,
            AsyncUtils asyncUtils) {
        this.fiscalCode = fiscalCode;
        this.citizenService = citizenService;
        this.citizenStatusUpdatePublisherService = citizenStatusUpdatePublisherService;
        super.asyncUtils = asyncUtils;
    }


    /**
     * Implementation of the MEDA Command doExecute method, contains the logic for the inbound transaction
     * management, calls the REST endpoint to check if it the related paymentInstrument is active, and eventually
     * sends the Transaction to the proper outbound channel. In case of an error, send a
     *
     * @return boolean to indicate if the command is succesfully executed
     */

    @SneakyThrows
    @Override
    public Boolean doExecute() {
        if (citizenService.delete(fiscalCode)) {
            super.callAsyncService(() -> {
                citizenStatusUpdatePublisherService.publishCitizenStatus(
                        StatusUpdate.builder()
                                .fiscalCode(fiscalCode)
                                .updateDateTime(OffsetDateTime.now())
                                .enabled(false)
                                .build()
                );
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
