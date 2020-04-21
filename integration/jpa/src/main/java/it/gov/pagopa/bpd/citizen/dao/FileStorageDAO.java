package it.gov.pagopa.bpd.citizen.dao;

import eu.sia.meda.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.citizen.dao.model.FileStorage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface FileStorageDAO extends CrudJpaDAO<FileStorage, String> {
    @Query(value = "select fs " +
            "from FileStorage fs " +
            "where :toodayDate between " +
            "fs.startDate and fs.endDate"
    )
    FileStorage getPdf(@Param("toodayDate") OffsetDateTime toodayDate);
}
