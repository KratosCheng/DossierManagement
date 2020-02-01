package edu.njusoftware.dossiermanagement.service;

import edu.njusoftware.dossiermanagement.domain.DossierContent;

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
}
