package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICaseService {
    CaseInfo getCaseInfo(String caseNum);

    List<CaseInfo> getAllCases();

    Page<CaseInfo> getCaseList(int pageNum, int pageSize);

    List<CaseInfo> getCasesByType(String type);

    boolean saveCase(CaseInfo caseInfo);

    BaseResponse removeCase(String caseNum);
}
