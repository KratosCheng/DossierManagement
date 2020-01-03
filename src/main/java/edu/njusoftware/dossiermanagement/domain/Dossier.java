package edu.njusoftware.dossiermanagement.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 程曦
 * 卷宗实体类
 */
@Entity(name = "entity_dossier")
public class Dossier {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    // 卷宗文件存储路径
    private String path;

    // 卷宗名称
    private String name;

    // 卷宗文件类型
    @Column(name = "file_type")
    private String fileType;

    // 卷宗所属案件案号
    @Column(name = "case_num")
    private String caseNum;

    // 卷宗所在文件夹
    private String directory;

    // 卷宗上传时间
    @Column(name = "upload_time")
    private Date uploadTime;

    // 卷宗上传人
    @Column(name = "upload_user")
    private String uploadUser;

    public Dossier(String path, String name, String fileType, String caseNum, String directory, Date uploadTime, String uploadUser) {
        this.path = path;
        this.name = name;
        this.fileType = fileType;
        this.caseNum = caseNum;
        this.directory = directory;
        this.uploadTime = uploadTime;
        this.uploadUser = uploadUser;
    }

    public Dossier() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    @Override
    public String toString() {
        return "Dossier{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", fileType='" + fileType + '\'' +
                ", caseNum='" + caseNum + '\'' +
                ", directory='" + directory + '\'' +
                ", uploadTime=" + uploadTime +
                ", uploadUser='" + uploadUser + '\'' +
                '}';
    }
}
