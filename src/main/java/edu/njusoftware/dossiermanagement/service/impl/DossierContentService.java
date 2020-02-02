package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.repository.DossierContentRepository;
import edu.njusoftware.dossiermanagement.repository.DossierOperationRecordRepository;
import edu.njusoftware.dossiermanagement.service.IDossierContentService;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DossierContentService implements IDossierContentService {

    @Autowired
    private DossierContentRepository dossierContentRepository;

    @Autowired
    private DossierOperationRecordRepository dossierOperationRecordRepository;

    @Autowired
    private IDossierService dossierService;

    @Override
    public DossierContent getDossierContent(long dossierId, int part) {
        return dossierContentRepository.findFirstByDossierIdAndPart(dossierId, part);
    }

    @Override
    public DossierContent rectifyDossierContent(long dossierId, int part, String content) {
        DossierContent dossierContent = getDossierContent(dossierId, part);
        if (!content.equals(dossierContent.getContent())) {
            Dossier dossier = dossierService.getDossier(dossierId);
            DossierOperationRecord dossierOperationRecord =
                    new DossierOperationRecord(SecurityUtils.getLoginUserName(), dossier.getCaseNum(), dossierId,
                            dossier.getName(), part, Constants.OPERATION_MODIFY, new Date(),
                            dossierContent.getContent(), content, 2);
            dossierOperationRecordRepository.save(dossierOperationRecord);
            dossierContent.setContent(content);
            return dossierContentRepository.save(dossierContent);
        }
        return dossierContent;
    }
}
