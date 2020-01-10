package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import edu.njusoftware.dossiermanagement.domain.User;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import edu.njusoftware.dossiermanagement.service.IUserService;
import edu.njusoftware.dossiermanagement.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    /**
     * 根据工号获取用户信息
     * @param model
     * @param jobNum
     * @return
     */
    @RequestMapping("/info/{jobNum}")
    public String getUserInfo(Model model, @PathVariable String jobNum) {
        User user = userService.getUserByJobNum(jobNum);
        model.addAttribute("userInfo", user);
        return "user::modify-user-div";
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
     * 根据用户提交表单刷新操作记录表
     * @param model
     * @param recordQueryCondition
     * @return
     */
    @RequestMapping("/hisRefresh")
    public String getOperationRecords(Model model, RecordQueryCondition recordQueryCondition) {
        User user = SecurityUtils.getLoginUser();
        if (!user.isAdmin()) {
            recordQueryCondition.setJobNum(user.getJobNum());
        }
        Page<OperationRecord> operationRecords = userService.getOperationRecords(recordQueryCondition);
        model.addAttribute("operationRecords", operationRecords);
        model.addAttribute("user", user);
        return "user::his-list-div";
    }

    /**
     * 重置操作记录表
     * @param model
     * @return
     */
    @RequestMapping("/hisReset")
    public String resetOperationRecords(Model model) {
        User user = SecurityUtils.getLoginUser();
        RecordQueryCondition recordQueryCondition = new RecordQueryCondition();
        recordQueryCondition.setPageNum(0);
        recordQueryCondition.setPageSize(10);
        if (!user.isAdmin()) {
            recordQueryCondition.setJobNum(user.getJobNum());
        }
        Page<OperationRecord> operationRecords = userService.getOperationRecords(recordQueryCondition);
        model.addAttribute("recordQueryCondition", recordQueryCondition);
        model.addAttribute("operationRecords", operationRecords);
        model.addAttribute("user", user);
        return "user::his-list-div";
    }

    /**
     * 根据管理员提交表单刷新账户表
     * @param model
     * @param accountQueryCondition
     * @return
     */
    @RequestMapping("/accountRefresh")
    public String getAccounts(Model model, AccountQueryCondition accountQueryCondition) {
        Page<User> users = userService.getUsers(accountQueryCondition);
        model.addAttribute("users", users);
        model.addAttribute("user", SecurityUtils.getLoginUser());
        return "user::account-list-div";
    }

    /**
     * 重置操作记录表
     * @param model
     * @return
     */
    @RequestMapping("/accountReset")
    public String resetAccounts(Model model) {
        AccountQueryCondition accountQueryCondition = new AccountQueryCondition();
        accountQueryCondition.setAccountPageNum(0);
        accountQueryCondition.setAccountPageSize(10);
        Page<User> users = userService.getUsers(accountQueryCondition);
        model.addAttribute("accountQueryCondition", accountQueryCondition);
        model.addAttribute("users", users);
        model.addAttribute("user", SecurityUtils.getLoginUser());
        return "user::account-list-div";
    }
}
