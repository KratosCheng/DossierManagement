package edu.njusoftware.dossiermanagement.domain.req;

public class CaseQueryCondition {
    // 显示第几页
    private int pageNum;

    // 每页显示条数
    private int pageSize;

    // 搜索关键词
    private String keyword;

    // 案件类型
    private String type;

    // 立案时间倒序
    private boolean descend;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDescend() {
        return descend;
    }

    public void setDescend(boolean descend) {
        this.descend = descend;
    }

    @Override
    public String toString() {
        return "CaseQueryCondition{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", keyword='" + keyword + '\'' +
                ", type='" + type + '\'' +
                ", descend=" + descend +
                '}';
    }
}
