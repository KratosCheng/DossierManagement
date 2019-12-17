package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;

import java.util.List;

public interface IDossierService {

    Dossier getDossier(long id);

    List<Dossier> getDossiersByCaseNum(String caseNum);

    boolean saveDossier(Dossier dossier);

    List<DossierOperationRecord> getDossierOperationRecordsByDossierId(long dossierId);

    List<DossierOperationRecord> getDossierOperationRecordsByCaseNum(String caseNum);

    List<DossierOperationRecord> getDossierOperationRecordsByJobNum(String jobNum);
}
