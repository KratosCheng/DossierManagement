package edu.njusoftware.dossiermanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity(name = "entity_user")
public class User {
    // 工号
    @Id
    @Column(name = "job_num")
    private String jobNum;

    // 密码
    @NotEmpty
    private String password;

    // 角色
    @Column(name = "role_name")
    private String roleName;

    // 用户登录状态
    private int status;

    public String getJobNum() {
        return jobNum;
    }

    public User(String jobNum, String password, String roleName, int status) {
        this.jobNum = jobNum;
        this.password = password;
        this.roleName = roleName;
        this.status = status;
    }

    public User() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
