package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.controller.UserController;
import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.repository.CaseRepository;
import edu.njusoftware.dossiermanagement.repository.DossierRepository;
import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SystemSecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private DossierRepository dossierRepository;

    @Override
    public CaseInfo getCaseInfo(String caseNum) {
        return caseRepository.findFirstByCaseNum(caseNum);
    }

    @Override
    public List<CaseInfo> getAllCases() {
        return caseRepository.findAll();
    }

    @Override
    public Page<CaseInfo> getCaseList(int pageNum, int pageSize) {
//        Sort sort = new Sort();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<CaseInfo> caseList = caseRepository.findAll(pageable);
        return caseList;
    }

    @Override
    public List<CaseInfo> getCasesByType(String type) {
        return null;
    }

    @Override
    public boolean saveCase(CaseInfo caseInfo) {
        return caseRepository.save(caseInfo) != null;
    }

    @Transactional
    @Override
    public BaseResponse removeCase(String caseNum) {
        StringBuilder processInfo = new StringBuilder(
                SystemSecurityUtils.getLoginUserName() + " remove case #" + caseNum + " with dossiers: ");
        List<Dossier> removedDossiers = dossierRepository.readAllByCaseNum(caseNum);
        for (Dossier dossier : removedDossiers) {
            processInfo.append(dossier.getId()).append("|");
        }
        if (caseRepository.removeByCaseNum(caseNum) == 0) {
            processInfo.append(" failed. Case #" + caseNum + " is not existed!");
            logger.error(processInfo.toString());
            return new BaseResponse(Constants.CODE_RESOURCE_NOT_FOUND, "未找到相应案件！");
        } else {
            processInfo.append(" succeed");
            logger.info(processInfo.toString());
            return new BaseResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS);
        }
    }
}
