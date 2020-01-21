package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.DossierContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DossierContentRepository extends JpaRepository<DossierContent, Long> {
}
