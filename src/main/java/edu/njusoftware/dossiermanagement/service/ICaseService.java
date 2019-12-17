package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.Case;

import java.util.List;

public interface ICaseService {
    Case getCaseInfo(String caseNum);

    List<Case> getAllCases();

    List<Case> getCasesByType(String type);

    boolean saveCase(Case caseInfo);
}
