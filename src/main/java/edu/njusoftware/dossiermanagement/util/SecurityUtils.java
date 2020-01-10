package edu.njusoftware.dossiermanagement.util;

import edu.njusoftware.dossiermanagement.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;

public class SecurityUtils {
    /**
     * 获取系统当前登录用户名
     * @return
     */
    public static String getLoginUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public static User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * md5加密用户密码
     * @param password
     * @return
     */
    public static String encodePassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
