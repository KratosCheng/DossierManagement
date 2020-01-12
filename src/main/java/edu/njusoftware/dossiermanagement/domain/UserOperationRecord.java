package edu.njusoftware.dossiermanagement.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户的修改记录
 */
@Entity(name = "his_user")
@DynamicInsert
@DynamicUpdate
public class UserOperationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String handler;

    @Column(name = "job_num")
    private String jobNum;

    private String operation;

    @Nullable
    private String attr;

    @Nullable
    @Column(name = "old_value")
    private String oldValue;

    @Nullable
    @Column(name = "current_value")
    private String currentValue;

    @Column(name = "operate_time")
    private Date operateTime;

    public UserOperationRecord() {
    }

    /**
     * 账号的修改操作
     * @param handler
     * @param jobNum
     * @param operation
     * @param attr
     * @param oldValue
     * @param currentValue
     * @param operateTime
     */
    public UserOperationRecord(String handler, String jobNum, String operation,
                               String attr, String oldValue, String currentValue, Date operateTime) {
        this.handler = handler;
        this.jobNum = jobNum;
        this.operation = operation;
        this.attr = attr;
        this.oldValue = oldValue;
        this.currentValue = currentValue;
        this.operateTime = operateTime;
    }

    /**
     * 账号的添加删除操作
     * @param handler
     * @param jobNum
     * @param operation
     * @param operateTime
     */
    public UserOperationRecord(String handler, String jobNum, String operation, Date operateTime) {
        this.handler = handler;
        this.jobNum = jobNum;
        this.operation = operation;
        this.operateTime = operateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Nullable
    public String getAttr() {
        return attr;
    }

    public void setAttr(@Nullable String attr) {
        this.attr = attr;
    }

    @Nullable
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(@Nullable String oldValue) {
        this.oldValue = oldValue;
    }

    @Nullable
    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(@Nullable String currentValue) {
        this.currentValue = currentValue;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return "UserOperationRecord{" +
                "id=" + id +
                ", operator='" + handler + '\'' +
                ", jobNum='" + jobNum + '\'' +
                ", operation='" + operation + '\'' +
                ", field='" + attr + '\'' +
                ", before='" + oldValue + '\'' +
                ", after='" + currentValue + '\'' +
                ", operateTime=" + operateTime +
                '}';
    }
}
