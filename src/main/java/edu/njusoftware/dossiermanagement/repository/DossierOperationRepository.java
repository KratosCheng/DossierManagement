package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DossierOperationRepository extends JpaRepository<DossierOperationRecord, Long> {
    List<DossierOperationRecord> findAllByDossierId(long dossierId);

    List<DossierOperationRecord> findAllByJobNum(String jobNum);

//    @Query(value = "select new DossierOperationRecord(id, ) from ")
//    List<DossierOperationRecord> findAllByCaseNum(String caseNum);
}
