package edu.njusoftware.dossiermanagement.domain;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

/**
 * @author 程曦
 * 案件实体类
 */
@Entity(name = "entity_case")
public class CaseInfo {

    // 案号
    @Id
    @NotEmpty(message = "案号不能为空")
    @Column(name = "case_num")
    private String caseNum;

    // 案由
    @NotEmpty(message = "案由不能为空")
    private String summary;

    // 立案时间
    @NotNull(message = "立案时间不能为空")
    @Column(name = "filing_time")
    private Date filingTime;

    // 结案时间
    @Nullable
    @Column(name = "closing_time")
    private Date closingTime;

    // 案件类型
    @NotNull(message = "案件类型不能为空")
    private String type;

    // 案件审理阶段
    private String stage;

    public CaseInfo(String caseNum, String summary, Date filingTime, Date closingTime, String type, String stage) {
        this.caseNum = caseNum;
        this.summary = summary;
        this.filingTime = filingTime;
        this.closingTime = closingTime;
        this.type = type;
        this.stage = stage;
    }

    public CaseInfo() {
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getFilingTime() {
        return filingTime;
    }

    public void setFilingTime(Date filingTime) {
        this.filingTime = filingTime;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseNum='" + caseNum + '\'' +
                ", summary='" + summary + '\'' +
                ", filingTime=" + filingTime +
                ", closingTime=" + closingTime +
                ", type='" + type + '\'' +
                ", stage='" + stage + '\'' +
                '}';
    }
}
