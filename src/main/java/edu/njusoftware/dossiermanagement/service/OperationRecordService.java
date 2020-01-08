package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import edu.njusoftware.dossiermanagement.repository.OperationRecordRepository;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OperationRecordService {
    private static OperationRecordRepository operationRecordRepository;

    @Autowired
    public OperationRecordService(OperationRecordRepository repository) {
        operationRecordRepository = repository;
    }

    /**
     * 存储案件的操作记录
     * @param caseNum
     * @param operation
     */
    public static void saveCaseOperationRecord(String caseNum, String operation) {
        OperationRecord record = new OperationRecord(SecurityUtils.getLoginUserName(), caseNum, operation, new Date());
        operationRecordRepository.save(record);
    }

    /**
     * 存储普通的卷宗操作记录
     * @param caseNum
     * @param dossierId
     * @param dossierName
     * @param operation
     */
    public static void saveNormalDossierOperationRecord(String caseNum, long dossierId, String dossierName, String operation) {
        OperationRecord record = new OperationRecord(SecurityUtils.getLoginUserName(), caseNum, dossierId, dossierName, operation, new Date());
        operationRecordRepository.save(record);
    }

    /**
     * 存储修正OCR结果的卷宗操作记录
     * @param caseNum
     * @param dossierId
     * @param dossierName
     * @param before
     * @param after
     */
    public static void saveOCRResultModificationOperationRecord(String caseNum, long dossierId, String dossierName, int pageNum, String before, String after) {
        OperationRecord record = new OperationRecord(SecurityUtils.getLoginUserName(), caseNum, dossierId, dossierName,
                pageNum, Constants.OPERATION_MODIFY, new Date(), before, after, 0);
        operationRecordRepository.save(record);
    }
}
