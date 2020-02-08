package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;

import java.util.List;

/**
 * 对卷宗文本内容进行操作的Service
 */
public interface IDossierContentService {
    /**
     * 获取卷宗某分页的文本内容
     * @param dossierId
     * @param part
     * @return
     */
    DossierContent getDossierContent(long dossierId, int part);

    /**
     * 修正卷宗文本内容
     * @param dossierId
     * @param content
     * @param part
     * @return
     */
    DossierContent rectifyDossierContent(long dossierId, int part, String content);

    /**
     * 获取卷宗某部分的修改记录
     * @param dossierId
     * @param part
     * @return
     */
    List<DossierOperationRecord> getContentHis(long dossierId, int part);

    /**
     * 重置卷宗文本内容
     * @param operationRecordId
     * @return
     */
    DossierContent resetDossierContent(long operationRecordId);
}
