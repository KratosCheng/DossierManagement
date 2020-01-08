package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRecordRepository extends JpaRepository<OperationRecord, Long>, JpaSpecificationExecutor<OperationRecord> {
    List<OperationRecord> findAllByDossierId(long dossierId);

    List<OperationRecord> findAllByJobNum(String jobNum);
}
