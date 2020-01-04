package edu.njusoftware.dossiermanagement.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 * 卷宗操作记录
 */
@Entity(name = "his_user_dossier")
@DynamicInsert
@DynamicUpdate
public class OperationRecord {
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

    @Column(name = "page_num")
    @Nullable
    private int pageNum;

    private String operation;

    @Column(name = "operate_time")
    private Date operateTime;

    @Nullable
    private String before;

    @Nullable
    private String after;

    private int status;

    public OperationRecord() {
    }

    /**
     * 卷宗操作记录
     * @param jobNum
     * @param caseNum
     * @param dossierId
     * @param operation
     * @param operateTime
     */
    public OperationRecord(String jobNum, String caseNum, long dossierId, String operation, Date operateTime) {
        this.jobNum = jobNum;
        this.caseNum = caseNum;
        this.dossierId = dossierId;
        this.operation = operation;
        this.operateTime = operateTime;
    }

    /**
     * 卷宗操作记录
     * @param jobNum
     * @param caseNum
     * @param dossierId
     * @param pageNum
     * @param operation
     * @param operateTime
     * @param before
     * @param after
     * @param status
     */
    public OperationRecord(String jobNum, String caseNum, long dossierId, int pageNum, String operation,
                           Date operateTime, String before, String after, int status) {
        this.jobNum = jobNum;
        this.caseNum = caseNum;
        this.dossierId = dossierId;
        this.pageNum = pageNum;
        this.operation = operation;
        this.operateTime = operateTime;
        this.before = before;
        this.after = after;
        this.status = status;
    }

    /**
     * 案件操作记录
     * @param jobNum
     * @param caseNum
     * @param operation
     * @param operateTime
     */
    public OperationRecord(String jobNum, String caseNum, String operation, Date operateTime) {
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

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
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
                ", before='" + before + '\'' +
                ", after='" + after + '\'' +
                ", status=" + status +
                '}';
    }
}
