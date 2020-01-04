package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.OperationRecord;

import java.util.List;
import java.util.Map;

public interface IDossierService {

    Dossier getDossier(long id);

    List<Dossier> getDossiersByCaseNum(String caseNum);

    boolean saveDossier(Dossier dossier);

    List<OperationRecord> getDossierOperationRecordsByDossierId(long dossierId);

    List<OperationRecord> getDossierOperationRecordsByCaseNum(String caseNum);

    List<OperationRecord> getDossierOperationRecordsByJobNum(String jobNum);

    boolean removeDossierById(long dossierId);

    Map<String, List<Dossier>> getDirectoryMap(String caseNum);

    List<String> getDirectoriesByCaseNum(String caseNum);

    void addDirectory(String caseNum, String directoryName);

    String getFilePathById(long dossierId);
}
