package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;

public interface DossierTextProcessor {
    void process(Dossier dossier);
}
