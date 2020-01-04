package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRecordRepository extends JpaRepository<OperationRecord, Long> {
    List<OperationRecord> findAllByDossierId(long dossierId);

    List<OperationRecord> findAllByJobNum(String jobNum);

//    @Query(value = "select new DossierOperationRecord(id, ) from ")
//    List<DossierOperationRecord> findAllByCaseNum(String caseNum);
}
