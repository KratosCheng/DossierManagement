package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseRepository extends JpaRepository<Case, String> {
    Case findFirstByCaseNum(String caseNum);

    List<Case> findAllByType(String type);

    int removeByCaseNum(String caseNum);
}
