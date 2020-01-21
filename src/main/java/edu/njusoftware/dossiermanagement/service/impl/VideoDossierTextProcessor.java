package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.FileEncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(Constants.FILE_TYPE_VIDEO)
public class VideoDossierTextProcessor implements DossierTextProcessor {

    @Autowired
    private AudioDossierTextProcessor audioDossierTextProcessor;

    @Override
    public void process(Dossier dossier) {
        audioDossierTextProcessor.processDossierAudioFile(dossier.getId(), dossier.getName(), dossier.getPath());
    }
}
