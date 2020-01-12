package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.UserOperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOperationRecordRepository extends JpaRepository<UserOperationRecord, Long>, JpaSpecificationExecutor<UserOperationRecord> {
}
