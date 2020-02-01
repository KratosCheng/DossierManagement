package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.repository.DossierContentRepository;
import edu.njusoftware.dossiermanagement.service.IDossierContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DossierContentService implements IDossierContentService {

    @Autowired
    private DossierContentRepository dossierContentRepository;

    @Override
    public DossierContent getDossierContent(long dossierId, int part) {
        return dossierContentRepository.findFirstByDossierIdAndPart(dossierId, part);
    }
}
