package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.repository.DossierContentRepository;
import edu.njusoftware.dossiermanagement.service.IDossierContentService;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DossierContentService implements IDossierContentService {

    @Autowired
    private DossierContentRepository dossierContentRepository;

    @Autowired
    private IDossierService dossierService;

    @Autowired
    private OperationRecordService operationRecordService;

    @Override
    public DossierContent getDossierContent(long dossierId, int part) {
        return dossierContentRepository.findFirstByDossierIdAndPart(dossierId, part);
    }

    @Override
    public DossierContent rectifyDossierContent(long dossierId, int part, String content) {
        DossierContent dossierContent = getDossierContent(dossierId, part);
        if (!content.equals(dossierContent.getContent())) {
            Dossier dossier = dossierService.getDossier(dossierId);
            // 存储修正记录
            operationRecordService.saveContentModificationOperationRecord(dossier.getCaseNum(), dossierId,
                    dossier.getName(), part, dossierContent.getContent(), content);
            dossierContent.setContent(content);
            return dossierContentRepository.save(dossierContent);
        }
        return dossierContent;
    }

    @Override
    public List<DossierOperationRecord> getContentHis(long dossierId, int part) {
        return operationRecordService.getDossierContentPartHis(dossierId, part);
    }
}
