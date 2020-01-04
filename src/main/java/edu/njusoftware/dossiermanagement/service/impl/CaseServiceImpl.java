package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.controller.UserController;
import edu.njusoftware.dossiermanagement.domain.CaseInfo;
import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import edu.njusoftware.dossiermanagement.domain.req.CaseQueryCondition;
import edu.njusoftware.dossiermanagement.repository.CaseRepository;
import edu.njusoftware.dossiermanagement.repository.OperationRecordRepository;
import edu.njusoftware.dossiermanagement.repository.DossierRepository;
import edu.njusoftware.dossiermanagement.domain.rsp.BaseResponse;
import edu.njusoftware.dossiermanagement.service.ICaseService;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CaseServiceImpl implements ICaseService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private OperationRecordRepository operationRepository;

    @Override
    public CaseInfo getCaseInfo(String caseNum) {
        // 存储删除操作记录
        OperationRecordService.saveCaseOperationRecord(caseNum, Constants.OPERATION_VIEW);

        return caseRepository.findFirstByCaseNum(caseNum);
    }

    @Override
    public List<CaseInfo> getAllCases() {
        return caseRepository.findAll();
    }

    @Override
    public Page<CaseInfo> getCaseList(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return caseRepository.findAll(pageable);
    }

    @Override
    public List<CaseInfo> getCasesByType(String type) {
        return null;
    }

    @Override
    public boolean saveCase(CaseInfo caseInfo) {
        if (caseRepository.save(caseInfo) == null) {
            return false;
        }
        // 存储添加操作记录
        OperationRecordService.saveCaseOperationRecord(caseInfo.getCaseNum(), Constants.OPERATION_ADD);
        return true;
    }

    @Transactional
    @Override
    public BaseResponse removeCase(String caseNum) {
        StringBuilder processInfo = new StringBuilder(
                SecurityUtils.getLoginUserName() + " remove case #" + caseNum + " with dossiers: ");
        List<Dossier> removedDossiers = dossierRepository.readAllByCaseNum(caseNum);
        for (Dossier dossier : removedDossiers) {
            processInfo.append(dossier.getId()).append("|");
        }

        if (caseRepository.removeByCaseNum(caseNum) == 0) {
            processInfo.append(" failed. Case #").append(caseNum).append(" is not existed!");
            logger.error(processInfo.toString());
            return new BaseResponse(Constants.CODE_RESOURCE_NOT_FOUND, "未找到相应案件！");
        }

        processInfo.append(" succeed");
        logger.info(processInfo.toString());
        // 存储删除操作记录
        OperationRecordService.saveCaseOperationRecord(caseNum, Constants.OPERATION_REMOVE);
        return new BaseResponse(Constants.CODE_SUCCESS, Constants.MSG_SUCCESS);
    }

    @Override
    public Page<CaseInfo> getCaseList(CaseQueryCondition caseQueryCondition) {
        Pageable pageable = PageRequest.of(caseQueryCondition.getPageNum(), caseQueryCondition.getPageSize(),
                caseQueryCondition.isDescend() ? Sort.Direction.DESC : Sort.Direction.ASC, "filingTime");
        Page<CaseInfo> caseInfoPage = caseRepository.findAll(new Specification<CaseInfo>(){
            @Override
            public Predicate toPredicate(Root<CaseInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                if(caseQueryCondition.getType() != null && !"".equals(caseQueryCondition.getType())) {
                    list.add(criteriaBuilder.equal(root.get("type").as(String.class), caseQueryCondition.getType()));
                }
                // 案号案由模糊查询
                if (caseQueryCondition.getKeyword() != null && !"".equals(caseQueryCondition.getKeyword())) {
                    String pattern = "%" + caseQueryCondition.getKeyword() + "%";
                    Predicate keywordPredicate =
                            criteriaBuilder.or(criteriaBuilder.like(root.get("caseNum").as(String.class), pattern),
                                    criteriaBuilder.like(root.get("summary").as(String.class), pattern));
                    list.add(keywordPredicate);
                }
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        }, pageable);
        return caseInfoPage;
    }
}
