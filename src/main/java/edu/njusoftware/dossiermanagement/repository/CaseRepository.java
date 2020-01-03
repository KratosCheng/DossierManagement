package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<CaseInfo, String>, JpaSpecificationExecutor<CaseInfo> {
    CaseInfo findFirstByCaseNum(String caseNum);

    List<CaseInfo> findAllByType(String type);

    int removeByCaseNum(String caseNum);
}
