package edu.njusoftware.dossiermanagement.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author 程曦
 * 用户实体类
 */
public class UserDetail extends User implements UserDetails {

    private List<Role> authorities;

    public UserDetail(String jobNum, String password, String roleId, String creator, Date createTime) {
        super(jobNum, password, roleId, creator, createTime);
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (Collection<? extends GrantedAuthority>) authorities;
    }

    @Override
    public String getUsername() {
        return getJobNum();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "authorities=" + authorities +
                '}';
    }
}
