package it.gov.pagopa.bpd.citizen.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Base implementation of the DeleteCitizenCommand, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class DeleteCitizenCommandImpl extends BaseCommand<Boolean> implements DeleteCitizenCommand {

    private CitizenService citizenService;
    private BeanFactory beanFactory;

    private String fiscalCode;

    public DeleteCitizenCommandImpl(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public DeleteCitizenCommandImpl(
            String fiscalCode,
            CitizenService citizenService,
            BeanFactory beanFactory) {
        this.fiscalCode = fiscalCode;
        this.citizenService = citizenService;
        this.beanFactory = beanFactory;
    }


    /**
     * Implementation of the MEDA Command doExecute method, contains the logic for the citizen deletion from the BPD program
     * management, calls the DAOs to deactivate the citizen and citizen ranking, and starts the async process to notify the
     * other services through a queue channel
     *
     * @return boolean to indicate if the command is succesfully executed
     */

    @SneakyThrows
    @Override
    public Boolean doExecute() {
        if (citizenService.delete(fiscalCode)) {
            SendAsyncEventCommand sendAsyncEventCommand = beanFactory.getBean(
                    SendAsyncEventCommand.class, fiscalCode);
            sendAsyncEventCommand.execute();
        }
        return true;
    }

    @Autowired
    public void setCitizenService(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @Autowired
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
