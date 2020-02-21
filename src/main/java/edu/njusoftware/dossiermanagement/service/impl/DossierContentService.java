package edu.njusoftware.dossiermanagement.service.impl;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierContent;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.repository.DossierContentRepository;
import edu.njusoftware.dossiermanagement.service.IDossierContentService;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.service.OperationRecordService;
import edu.njusoftware.dossiermanagement.util.Constants;
import edu.njusoftware.dossiermanagement.util.StringDifferenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

@Service
public class DossierContentService implements IDossierContentService {

    @Autowired
    private DossierContentRepository dossierContentRepository;

    @Autowired
    private IDossierService dossierService;

    @Autowired
    private OperationRecordService operationRecordService;

    @Value("${content.rectify.maxLength}")
    private int maxRectifyLength;

    @Override
    public DossierContent getDossierContent(long dossierId, int part) {
        return dossierContentRepository.findFirstByDossierIdAndPart(dossierId, part);
    }

    @Override
    public DossierContent rectifyDossierContent(long dossierId, int part, String content) {
        DossierContent dossierContent = getDossierContent(dossierId, part);
        if (dossierContent.getContent().length() != content.length() || StringUtils.isEmpty(content)) {
            return null;
        }
        if (!content.equals(dossierContent.getContent())) {
            Dossier dossier = dossierService.getDossier(dossierId);
            String newLocationInfo = null;
            if (Constants.FILE_TYPE_PDF.equals(dossier.getFileType())) {
                // todo
            } else {
                newLocationInfo = rectifyTimeInfo(dossierContent.getContent(), content, dossierContent.getLocationInfo().split("_"));
                dossierContent.setLocationInfo(newLocationInfo);
            }
            // 存储修正记录
            operationRecordService.saveContentModificationOperationRecord(dossier.getCaseNum(), dossierId,
                    dossier.getName(), part, dossierContent.getContent(), newLocationInfo, content);
            dossierContent.setContent(content);
            return dossierContentRepository.save(dossierContent);
        }
        return dossierContent;
    }

    @Override
    public List<DossierOperationRecord> getContentHis(long dossierId, int part) {
        return operationRecordService.getDossierContentPartHis(dossierId, part);
    }

    @Override
    public DossierContent resetDossierContent(long operationRecordId) {
        DossierOperationRecord record = operationRecordService.getDossierOperationRecordById(operationRecordId);
        DossierContent dossierContent = getDossierContent(record.getDossierId(), record.getPageNum());
        dossierContent.setLocationInfo(record.getAdditionalInfo());
        operationRecordService.saveContentModificationOperationRecord(record.getCaseNum(), record.getDossierId(),
                record.getDossierName(), record.getPageNum(), dossierContent.getContent(), record.getCurrentValue(),
                record.getAdditionalInfo());
        dossierContent.setContent(record.getCurrentValue());
        return dossierContentRepository.save(dossierContent);
    }

    /**
     * 修正卷宗文本内容的时间信息，使用最短编辑距离算法计算最小更改差别
     * @param before
     * @param after
     * @param oldTimeInfo
     * @return
     */
    private static String rectifyTimeInfo(String before, String after, String[] oldTimeInfo) {
        int beforeIndex = 0;
        int afterIndex = 0;
        int beforeLength = before.length();
        int afterLength = after.length();
        String[] diff = StringDifferenceUtils.getTempString(before, after);
        String beforeDiff = diff[0], afterDiff = diff[1];
        StringBuilder newTimeInfo = new StringBuilder(afterLength);
        while (beforeIndex < beforeLength && afterIndex < afterLength) {
            if (beforeDiff.charAt(beforeIndex) == afterDiff.charAt(afterIndex)) {
                // 无改变或者是字符变更
                newTimeInfo.append(oldTimeInfo[beforeIndex]).append(Constants.TIME_INFO_SEPARATOR);
                beforeIndex++;
                afterIndex++;
            } else if (' ' == beforeDiff.charAt(beforeIndex)) {
                // before为空，after不为空，before的当前字符被删除
                beforeIndex++;
            } else if (' ' == afterDiff.charAt(afterIndex)){
                // before不为空，after为空，before的当前字符前被插入新字符
                newTimeInfo.append(oldTimeInfo[beforeIndex]).append(Constants.TIME_INFO_SEPARATOR);
                afterIndex++;
            }
        }

        beforeIndex = beforeIndex < beforeLength ? beforeIndex : beforeLength - 1;

        while (afterIndex < afterLength) {
            newTimeInfo.append(oldTimeInfo[beforeIndex]).append(Constants.TIME_INFO_SEPARATOR);
            afterIndex++;
        }
        return newTimeInfo.substring(0, newTimeInfo.length() - 1);
    }

//    public static void test() {
//        String a = "我是中国人，爱说中国话！";
//        String b = "因为我是中国人，所以爱说国语！";
//        String[] timeInfo = "0_1_2_3_4_5_6_7_8_9_10_11".split("_");
//        rectifyTimeInfo(a, b, timeInfo);
//    }

    public List<DossierContent> search(String keyword, Set<Long> dossierIds) {
        return dossierContentRepository.findAll(new Specification<DossierContent>() {
            @Override
            public Predicate toPredicate(Root<DossierContent> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> path = root.get("dossierId");
                criteriaQuery.where(path.in(dossierIds));
                String pattern = "%" + keyword + "%";
                return criteriaBuilder.like(root.get("content").as(String.class), pattern);
            }
        });
    }
}
