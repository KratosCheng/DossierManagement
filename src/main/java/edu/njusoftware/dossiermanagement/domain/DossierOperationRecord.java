package edu.njusoftware.dossiermanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 卷宗操作记录
 */
@Entity(name = "his_user_dossier")
public class DossierOperationRecord {
    @Id
    private long id;

    @Column(name = "job_num")
    private String jobNum;

    @Column(name = "dossier_id")
    private long dossierId;

    private String operation;

    @Column(name = "operate_time")
    private Date operateTime;

    private String before;

    private String after;

    private int status;

    public DossierOperationRecord(long id, String jobNum, long dossierId, String operation, Date operateTime, String before, String after, int status) {
        this.id = id;
        this.jobNum = jobNum;
        this.dossierId = dossierId;
        this.operation = operation;
        this.operateTime = operateTime;
        this.before = before;
        this.after = after;
        this.status = status;
    }

    public DossierOperationRecord() {
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

    public long getDossierId() {
        return dossierId;
    }

    public void setDossierId(long dossierId) {
        this.dossierId = dossierId;
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
}
