package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.Case;
import edu.njusoftware.dossiermanagement.rsp.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICaseService {
    Case getCaseInfo(String caseNum);

    List<Case> getAllCases();

    Page<Case> getCaseList(int pageNum, int pageSize);

    List<Case> getCasesByType(String type);

    boolean saveCase(Case caseInfo);

    BaseResponse removeCase(String caseNum);
}
