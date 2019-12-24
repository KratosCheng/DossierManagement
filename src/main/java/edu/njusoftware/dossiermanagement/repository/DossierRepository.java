package edu.njusoftware.dossiermanagement.repository;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long> {

    List<Dossier> findAllByCaseNum(String caseNum);

    Dossier removeById(long id);

    List<Dossier> readAllByCaseNum(String caseNum);
}
