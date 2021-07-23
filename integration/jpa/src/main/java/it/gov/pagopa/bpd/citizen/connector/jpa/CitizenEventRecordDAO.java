package it.gov.pagopa.bpd.citizen.connector.jpa;

import it.gov.pagopa.bpd.citizen.connector.jpa.model.Citizen;
import it.gov.pagopa.bpd.citizen.connector.jpa.model.CitizenEventRecord;
import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface CitizenEventRecordDAO extends CrudJpaDAO<CitizenEventRecord, String> {

    @Override
    CitizenEventRecord save(@NotNull CitizenEventRecord entity);

    @Query(value = "SELECT cer FROM CitizenEventRecord cer WHERE" +
            "(fiscalCode IS NULL OR cer.fiscalCode = :fiscalCode) AND " +
            "cer.sentTimestamp IS NULL AND cer.enabled = true")
    List<CitizenEventRecord> retrieveEventToSend(String fiscalCode);

}