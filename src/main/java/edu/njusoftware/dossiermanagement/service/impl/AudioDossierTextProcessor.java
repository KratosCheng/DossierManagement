package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.service.DossierTextProcessor;
import edu.njusoftware.dossiermanagement.util.Constants;
import org.springframework.stereotype.Service;

@Service(Constants.FILE_TYPE_AUDIO)
public class AudioDossierTextProcessor implements DossierTextProcessor {
    @Override
    public void process(Dossier dossier) {

    }
}
