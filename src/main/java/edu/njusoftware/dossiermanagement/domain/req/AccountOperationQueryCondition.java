package edu.njusoftware.dossiermanagement.domain.req;

public class AccountOperationQueryCondition {
    // 显示第几页
    private int operationPageNum;

    // 每页显示条数
    private int operationPageSize;

    // 搜索关键词
    private String operationKeyword;

    // 操作时间倒序
    private boolean operationDescend;

    // 操作类型
    private String operationType;

    public int getOperationPageNum() {
        return operationPageNum;
    }

    public AccountOperationQueryCondition(int operationPageNum, int operationPageSize) {
        this.operationPageNum = operationPageNum;
        this.operationPageSize = operationPageSize;
    }

    public AccountOperationQueryCondition() {
    }

    public void setOperationPageNum(int operationPageNum) {
        this.operationPageNum = operationPageNum;
    }

    public int getOperationPageSize() {
        return operationPageSize;
    }

    public void setOperationPageSize(int operationPageSize) {
        this.operationPageSize = operationPageSize;
    }

    public String getOperationKeyword() {
        return operationKeyword;
    }

    public void setOperationKeyword(String operationKeyword) {
        this.operationKeyword = operationKeyword;
    }

    public boolean isOperationDescend() {
        return operationDescend;
    }

    public void setOperationDescend(boolean operationDescend) {
        this.operationDescend = operationDescend;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "AccountOperationQueryCondition{" +
                "operationPageNum=" + operationPageNum +
                ", operationPageSize=" + operationPageSize +
                ", operationKeyword='" + operationKeyword + '\'' +
                ", operationDescend=" + operationDescend +
                ", operation='" + operationType + '\'' +
                '}';
    }
}
