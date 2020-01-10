package edu.njusoftware.dossiermanagement.domain;

import edu.njusoftware.dossiermanagement.util.Constants;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "entity_user")
public class User {
    // 工号
    @Id
    @NotEmpty(message = "工号不能为空")
    @Length(min = 8, max = 8, message = "工号长度为8")
    @Column(name = "job_num")
    private String jobNum;

    // 密码
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, message = "密码最短为6位")
    private String password;

    // 角色
    @NotNull
    @Column(name = "role_name")
    private String roleName;

    // 创建者
    private String creator;

    // 创建的时间
    @Column(name = "create_time")
    private Date createTime;

    public User(String jobNum, String password, String roleName, String creator, Date createTime) {
        this.jobNum = jobNum;
        this.password = password;
        this.roleName = roleName;
        this.creator = creator;
        this.createTime = createTime;
    }

    public User() {
    }

    public boolean isAdmin() {
        return Constants.ROLE_ADMIN.equals(roleName);
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "jobNum='" + jobNum + '\'' +
                ", password='" + password + '\'' +
                ", roleName='" + roleName + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
