package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.DossierContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DossierContentRepository extends JpaRepository<DossierContent, Long>, JpaSpecificationExecutor<DossierContent> {
    DossierContent findFirstByDossierIdAndPart(long dossierId, int part);
}
