package edu.njusoftware.dossiermanagement.domain.rsp;

/**
 * 案件内关键字搜索结果
 */
public class CaseSearchResult {
    // 卷宗id
    private long dossierId;

    // 卷宗名称
    private String dossierName;

    // 关键字所在卷宗部分
    private int part;

    // 卷宗类型
    private String type;

    // 关键字结果示例（取左右各五个字）
    private String example;

    private String location;

    public CaseSearchResult(long dossierId, String dossierName, int part, String type, String example, String location) {
        this.dossierId = dossierId;
        this.dossierName = dossierName;
        this.part = part;
        this.type = type;
        this.example = example;
        this.location = location;
    }

    public long getDossierId() {
        return dossierId;
    }

    public void setDossierId(long dossierId) {
        this.dossierId = dossierId;
    }

    public String getDossierName() {
        return dossierName;
    }

    public void setDossierName(String dossierName) {
        this.dossierName = dossierName;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CaseSearchResult{" +
                "dossierId=" + dossierId +
                ", dossierName='" + dossierName + '\'' +
                ", part=" + part +
                ", type='" + type + '\'' +
                ", example='" + example + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
