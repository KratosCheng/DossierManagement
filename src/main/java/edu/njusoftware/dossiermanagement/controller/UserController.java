package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.config.Constants;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    /**
     * 根据工号获取用户信息
     * @param jobNum
     * @return
     */
    @RequestMapping("/info/{jobNum}")
    public User getUserInfo(@PathVariable String jobNum) {
        return userService.getUserByJobNum(jobNum);
    }

    /**
     * 获取所有用户的信息
     * @return
     */
    @RequestMapping("/list/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 获取某角色下的所有用户的信息
     * @return
     */
    @RequestMapping("/list/{roleName}")
    public List<User> getAllUsersByRole(@PathVariable String roleName) {
        return userService.getAllUsersByRoleName(roleName);
    }
}
