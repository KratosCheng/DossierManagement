package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.OperationRecord;
import edu.njusoftware.dossiermanagement.domain.User;
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

    @RequestMapping("/refresh")
    public String getOperationRecords(Model model, @ModelAttribute RecordQueryCondition recordQueryCondition) {
        Page<OperationRecord> operationRecords = userService.getOperationRecords(recordQueryCondition);
        model.addAttribute("operationRecords", operationRecords);
        model.addAttribute("user", userService.getUserByJobNum(SecurityUtils.getLoginUserName()));
        return "user";
    }
}
