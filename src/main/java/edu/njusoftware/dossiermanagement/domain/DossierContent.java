package edu.njusoftware.dossiermanagement.domain;

import org.springframework.lang.Nullable;

import javax.persistence.*;

/**
 * @author 程曦
 * 卷宗文本内容实体类
 */
@Entity(name = "entity_dossier_content")
public class DossierContent {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    // 卷宗id
    @Column(name = "dossier_id")
    private long dossierId;

    // 卷宗文件类型
    @Column(name = "file_type")
    private String fileType;

    // pdf的第几页，音视频卷宗的第几个部分（60s）
    private int part;

    // 位置信息
    @Column(name = "location_info")
    private String locationInfo;

    // 纯文本内容
    private String content;

    // 附件信息、存储初始识别结果
    @Nullable
    @Column(name = "additional_info")
    private String additionalInfo;

    public DossierContent(long dossierId, String fileType, int part, String locationInfo, String content) {
        this.dossierId = dossierId;
        this.fileType = fileType;
        this.part = part;
        this.locationInfo = locationInfo;
        this.content = content;
    }

    public DossierContent(long dossierId, String fileType, int part, String locationInfo, String content, @Nullable String additionalInfo) {
        this.dossierId = dossierId;
        this.fileType = fileType;
        this.part = part;
        this.locationInfo = locationInfo;
        this.content = content;
        this.additionalInfo = additionalInfo;
    }

    public DossierContent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDossierId() {
        return dossierId;
    }

    public void setDossierId(long dossierId) {
        this.dossierId = dossierId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return "DossierContent{" +
                "id=" + id +
                ", dossierId=" + dossierId +
                ", fileType='" + fileType + '\'' +
                ", part=" + part +
                ", locationInfo='" + locationInfo + '\'' +
                ", content='" + content + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
