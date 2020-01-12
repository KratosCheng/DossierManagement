package edu.njusoftware.dossiermanagement.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 * 卷宗操作记录
 */
@Entity(name = "his_dossier")
@DynamicInsert
@DynamicUpdate
public class DossierOperationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "job_num")
    private String jobNum;

    @Column(name = "case_num")
    private String caseNum;

    @Column(name = "dossier_id")
    @Nullable
    private long dossierId;

    @Column(name = "dossier_name")
    @Nullable
    private String dossierName;

    @Column(name = "page_num")
    @Nullable
    private int pageNum;

    private String operation;

    @Column(name = "operate_time")
    private Date operateTime;

    @Nullable
    @Column(name = "old_value")
    private String oldValue;

    @Nullable
    @Column(name = "current_value")
    private String currentValue;

    private int status;

    public DossierOperationRecord() {
    }

    /**
     * 卷宗操作记录
     * @param jobNum
     * @param caseNum
     * @param dossierId
     * @param dossierName 
     * @param operation
     * @param operateTime
     */
    public DossierOperationRecord(String jobNum, String caseNum, long dossierId, String dossierName, String operation, Date operateTime) {
        this.jobNum = jobNum;
        this.caseNum = caseNum;
        this.dossierId = dossierId;
        this.dossierName = dossierName;
        this.operation = operation;
        this.operateTime = operateTime;
    }

    /**
     * 卷宗操作记录
     * @param jobNum
     * @param caseNum
     * @param dossierId
     * @param dossierName
     * @param pageNum
     * @param operation
     * @param operateTime
     * @param oldValue
     * @param currentValue
     * @param status
     */
    public DossierOperationRecord(String jobNum, String caseNum, long dossierId, String dossierName, int pageNum,
                                  String operation, Date operateTime, String oldValue, String currentValue, int status) {
        this.jobNum = jobNum;
        this.caseNum = caseNum;
        this.dossierId = dossierId;
        this.dossierName = dossierName;
        this.pageNum = pageNum;
        this.operation = operation;
        this.operateTime = operateTime;
        this.oldValue = oldValue;
        this.currentValue = currentValue;
        this.status = status;
    }

    /**
     * 案件操作记录
     * @param jobNum
     * @param caseNum
     * @param operation
     * @param operateTime
     */
    public DossierOperationRecord(String jobNum, String caseNum, String operation, Date operateTime) {
        this.jobNum = jobNum;
        this.caseNum = caseNum;
        this.operation = operation;
        this.operateTime = operateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public long getDossierId() {
        return dossierId;
    }

    public void setDossierId(long dossierId) {
        this.dossierId = dossierId;
    }

    @Nullable
    public String getDossierName() {
        return dossierName;
    }

    public void setDossierName(@Nullable String dossierName) {
        this.dossierName = dossierName;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OperationRecord{" +
                "id=" + id +
                ", jobNum='" + jobNum + '\'' +
                ", caseNum='" + caseNum + '\'' +
                ", dossierId=" + dossierId +
                ", pageNum=" + pageNum +
                ", operation='" + operation + '\'' +
                ", operateTime=" + operateTime +
                ", before='" + oldValue + '\'' +
                ", after='" + currentValue + '\'' +
                ", status=" + status +
                '}';
    }
}
