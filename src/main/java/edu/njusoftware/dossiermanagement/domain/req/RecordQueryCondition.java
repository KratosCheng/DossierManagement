package edu.njusoftware.dossiermanagement.domain.req;

public class RecordQueryCondition {
    // 显示第几页
    private int pageNum;

    // 每页显示条数
    private int pageSize;

    // 搜索关键词
    private String keyword;

    // 操作类型
    private String operation;

    // 操作时间倒序
    private boolean descend;

    // 工号
    private String jobNum;

    // 修改操作状态
    private int status;

    public RecordQueryCondition(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public RecordQueryCondition(int pageNum, int pageSize, int status) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.status = status;
    }

    public RecordQueryCondition() {
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isDescend() {
        return descend;
    }

    public void setDescend(boolean descend) {
        this.descend = descend;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RecordQueryCondition{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", keyword='" + keyword + '\'' +
                ", operation='" + operation + '\'' +
                ", descend=" + descend +
                ", jobNum='" + jobNum + '\'' +
                ", status=" + status +
                '}';
    }
}
