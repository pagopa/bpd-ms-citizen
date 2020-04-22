package it.gov.pagopa.bpd.file_storage.dao;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.citizen.dao.FileStorageDAO;
import it.gov.pagopa.bpd.citizen.dao.model.FileStorage;
import it.gov.pagopa.bpd.common.BaseCrudJpaDAOTest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class FileStorageDAOTest extends BaseCrudJpaDAOTest<FileStorageDAO, FileStorage, Long> {

    @Autowired
    private FileStorageDAO fileStorageDAO;

    @Override
    protected CriteriaQuery<? super FileStorage> getMatchAlreadySavedCriteria() {
        FileStorageDAOTest.FileStorageCriteria criteriaQuery = new FileStorageDAOTest.FileStorageCriteria();
        criteriaQuery.setId(getStoredId());

        return criteriaQuery;
    }

    @Override
    protected FileStorageDAO getCitizenDAO() {
        return fileStorageDAO;
    }

    @Override
    protected void setId(FileStorage entity, Long id) {
        entity.setId(id);
    }

    @Override
    protected Long getId(FileStorage entity) {
        return entity.getId();
    }

    @Override
    protected void alterEntityToUpdate(FileStorage entity) {
        entity.setStartDate(OffsetDateTime.now());
    }

    @Override
    protected Function<Integer, Long> idBuilderFn() {
        return (bias) -> null;
    }

    @Data
    private static class FileStorageCriteria implements CriteriaQuery<FileStorage> {
        private Long id;
    }
}
