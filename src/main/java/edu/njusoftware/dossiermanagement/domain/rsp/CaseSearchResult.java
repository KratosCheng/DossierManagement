package edu.njusoftware.dossiermanagement.domain.rsp;

/**
 * 案件内关键字搜索结果
 */
public class CaseSearchResult {
    // 卷宗id
    private long dossierId;

    // 卷宗所在目录
    private String directory;

    // 卷宗名称
    private String dossierName;

    // 关键字所在卷宗部分
    private int part;

    // 卷宗类型
    private String type;

    // 关键字结果示例（取左右各五个字）
    private String example;

    public CaseSearchResult(long dossierId, String directory, String dossierName, int part, String type, String example) {
        this.dossierId = dossierId;
        this.directory = directory;
        this.dossierName = dossierName;
        this.part = part;
        this.type = type;
        this.example = example;
    }

    public long getDossierId() {
        return dossierId;
    }

    public void setDossierId(long dossierId) {
        this.dossierId = dossierId;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
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

    @Override
    public String toString() {
        return "CaseSearchResult{" +
                "dossierId=" + dossierId +
                ", directory='" + directory + '\'' +
                ", dossierName='" + dossierName + '\'' +
                ", part=" + part +
                ", type='" + type + '\'' +
                ", example='" + example + '\'' +
                '}';
    }
}
