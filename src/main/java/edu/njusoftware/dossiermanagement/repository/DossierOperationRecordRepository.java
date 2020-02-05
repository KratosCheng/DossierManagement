package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DossierOperationRecordRepository extends JpaRepository<DossierOperationRecord, Long>, JpaSpecificationExecutor<DossierOperationRecord> {
    List<DossierOperationRecord> findAllByDossierId(long dossierId);

    List<DossierOperationRecord> findAllByJobNum(String jobNum);

    List<DossierOperationRecord> findAllByDossierIdAndPageNumAndOperation(long dossierId, int pageNum, String operation, Sort sort);
}
