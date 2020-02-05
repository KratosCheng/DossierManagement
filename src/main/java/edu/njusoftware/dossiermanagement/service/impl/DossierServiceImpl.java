package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.mapper.DossierMapper;
import edu.njusoftware.dossiermanagement.repository.DossierOperationRecordRepository;
import edu.njusoftware.dossiermanagement.repository.DossierRepository;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessorFactory;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import edu.njusoftware.dossiermanagement.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DossierServiceImpl implements IDossierService {

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierOperationRecordRepository dossierOperationRecordRepository;

    @Autowired
    private DossierMapper dossierMapper;

    @Autowired
    private DossierTextProcessorFactory dossierTextProcessorFactory;

    @Autowired
    private OperationRecordService operationRecordService;

    @Override
    public Dossier getDossier(long id) {
        Optional<Dossier> optionalDossier = dossierRepository.findById(id);
        return optionalDossier.orElse(null);
    }

    @Override
    public List<Dossier> getDossiersByCaseNum(String caseNum) {
        return dossierRepository.findAllByCaseNum(caseNum);
    }

    @Override
    public boolean saveDossier(Dossier dossier) {
        Dossier savedDossier = dossierRepository.save(dossier);
        if (savedDossier == null) {
            return false;
        }
        operationRecordService.saveNormalDossierOperationRecord(dossier.getCaseNum(), savedDossier.getId(),
                savedDossier.getName(), Constants.OPERATION_ADD);
        return true;
    }

    @Override
    public List<DossierOperationRecord> getDossierOperationRecordsByDossierId(long dossierId) {
        return dossierOperationRecordRepository.findAllByDossierId(dossierId);
    }

    @Override
    public List<DossierOperationRecord> getDossierOperationRecordsByCaseNum(String caseNum) {
        return dossierMapper.findRecordsByCaseNum(caseNum);
    }

    @Override
    public List<DossierOperationRecord> getDossierOperationRecordsByJobNum(String jobNum) {
        return dossierOperationRecordRepository.findAllByJobNum(jobNum);
    }

    @Override
    public boolean removeDossierById(long dossierId) {
        Dossier removedDossier = dossierRepository.removeById(dossierId);
        if (removedDossier == null) {
            return false;
        }
        operationRecordService.saveNormalDossierOperationRecord(removedDossier.getCaseNum(), removedDossier.getId(),
                removedDossier.getName(), Constants.OPERATION_REMOVE);
        return true;
    }

    /**
     * 获取当前案件卷宗目录映射
     * @param caseNum
     * @return
     */
    @Override
    public Map<String, List<Dossier>> getDirectoryMap(String caseNum) {
        List<Dossier> dossierList = getDossiersByCaseNum(caseNum);
        // 构建卷宗目录映射
        Map<String, List<Dossier>> directoryMap = new HashMap<>();
        for (Dossier dossier : dossierList) {
            String directory = dossier.getDirectory();
            if (directoryMap.containsKey(directory)) {
                directoryMap.get(directory).add(dossier);
            } else {
                List<Dossier> dossierListInDirectory = new LinkedList<>();
                dossierListInDirectory.add(dossier);
                directoryMap.put(directory, dossierListInDirectory);
            }
        }
        return directoryMap;
    }

    @Override
    public List<String> getDirectoriesByCaseNum(String caseNum) {
        return dossierMapper.findDirectoriesByCaseNum(caseNum);
    }

    @Transactional
    @Override
    public void addDirectory(String caseNum, String directoryName) {
        dossierMapper.addDirectory(caseNum, directoryName);
    }

    @Override
    public String getFilePathById(long dossierId) {
        return dossierMapper.getFilePathById(dossierId);
    }

    /**
     *
     * @param dossier
     */
    @Override
    public void processDossierContent(Dossier dossier) {
        DossierTextProcessor processor = dossierTextProcessorFactory.getDossierTextProcessor(dossier.getFileType());
        processor.process(dossier);
    }
}
