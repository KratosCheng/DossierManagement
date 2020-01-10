package edu.njusoftware.dossiermanagement.domain.req;

public class AccountQueryCondition {
    // 显示第几页
    private int accountPageNum;

    // 每页显示条数
    private int accountPageSize;

    // 搜索关键词
    private String accountKeyword;

    // 工号倒序
    private boolean accountDescend;

    // 用户角色
    private String role;

    public int getAccountPageNum() {
        return accountPageNum;
    }

    public void setAccountPageNum(int accountPageNum) {
        this.accountPageNum = accountPageNum;
    }

    public int getAccountPageSize() {
        return accountPageSize;
    }

    public void setAccountPageSize(int accountPageSize) {
        this.accountPageSize = accountPageSize;
    }

    public String getAccountKeyword() {
        return accountKeyword;
    }

    public void setAccountKeyword(String accountKeyword) {
        this.accountKeyword = accountKeyword;
    }

    public boolean isAccountDescend() {
        return accountDescend;
    }

    public void setAccountDescend(boolean accountDescend) {
        this.accountDescend = accountDescend;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AccountQueryCondition{" +
                "pageNum=" + accountPageNum +
                ", pageSize=" + accountPageSize +
                ", keyword='" + accountKeyword + '\'' +
                ", descend=" + accountDescend +
                ", role='" + role + '\'' +
                '}';
    }
}
