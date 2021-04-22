package it.gov.pagopa.bpd.citizen.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.model.TransactionCommandModel;
import it.gov.pagopa.bpd.citizen.publisher.model.Transaction;
import it.gov.pagopa.bpd.citizen.service.CitizenService;
import it.gov.pagopa.bpd.citizen.service.PointTransactionPublisherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.*;
import java.util.Set;

/**
 * Base implementation of the SaveTransactionCommandInterface, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class FilterTransactionCommandImpl extends BaseCommand<Boolean> implements FilterTransactionCommand {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    private PointTransactionPublisherService pointTransactionProducerService;
    private CitizenService citizenService;

    private final TransactionCommandModel transactionCommandModel;


    public FilterTransactionCommandImpl(TransactionCommandModel transactionCommandModel) {
        this.transactionCommandModel = transactionCommandModel;
    }

    public FilterTransactionCommandImpl(
            TransactionCommandModel transactionCommandModel,
            CitizenService citizenService,
            PointTransactionPublisherService pointTransactionProducerService) {
        this.transactionCommandModel = transactionCommandModel;
        this.citizenService = citizenService;
        this.pointTransactionProducerService = pointTransactionProducerService;
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

        Transaction transaction = transactionCommandModel.getPayload();

        try {

            validateRequest(transaction);
            Citizen citizen = citizenService.find(transaction.getFiscalCode());

            if (citizen.isEnabled() && citizen.getTimestampTC().isBefore(transaction.getTrxDate())) {

                pointTransactionProducerService.publishPointTransactionEvent(transaction);

            } else {
                log.info("Met a transaction for an inactive citizen on BPD. [{}, {}, {}]",
                        transaction.getIdTrxAcquirer(), transaction.getAcquirerCode(), transaction.getTrxDate());
            }

            return true;

        } catch (Exception e) {

            if (transaction != null) {

                if (logger.isErrorEnabled()) {
                    logger.error("Error occured during processing for transaction: " +
                            transaction.getIdTrxAcquirer() + ", " +
                            transaction.getAcquirerCode() + ", " +
                            transaction.getTrxDate());
                    logger.error(e.getMessage(), e);
                }

            }

            throw e;

        }
    }

    @Autowired
    public void setPointTransactionProducerService(
            PointTransactionPublisherService pointTransactionProducerService) {
        this.pointTransactionProducerService = pointTransactionProducerService;
    }

    @Autowired
    public void setCitizenService(CitizenService citizenService) {
        this.citizenService = citizenService;
    }


    /**
     * Method to process a validation check for the parsed Transaction request
     *
     * @param request instance of Transaction, parsed from the inbound byte[] payload
     * @throws ConstraintViolationException
     */
    private void validateRequest(Transaction request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() > 0) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }


}
