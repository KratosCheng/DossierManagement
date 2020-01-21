package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.Dossier;
import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.domain.Account;
import edu.njusoftware.dossiermanagement.domain.UserOperationRecord;
import edu.njusoftware.dossiermanagement.domain.req.AccountOperationQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.service.IUserService;
import edu.njusoftware.dossiermanagement.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService userService;

    @Autowired
    private IDossierService dossierService;

    /**
     * 根据工号获取用户信息
     * @param model
     * @param jobNum
     * @return
     */
    @RequestMapping("/info/{jobNum}")
    public String getUserInfo(Model model, @PathVariable String jobNum) {
        Account user = userService.getUserByJobNum(jobNum);
        model.addAttribute("userInfo", user);
        return "user::modify-user-div";
    }

    /**
     * 获取所有用户的信息
     * @return
     */
    @RequestMapping("/list/all")
    public String getAllUsers() throws IOException, ParseException {
        Dossier dossier = dossierService.getDossier(66);
        dossierService.processDossierContent(dossier);
//        List<String> strings = IATSpeechRecognizer.RecognizePcmfileByte(pcmPath);
//        FileEncodeUtils.convertingAudioToPcmFormat(sourcePath, pcmPath, 0F, 15F);
        return "hahah";
    }

    /**
     * 根据用户提交表单刷新操作记录表
     * @param model
     * @param recordQueryCondition
     * @return
     */
    @RequestMapping("/hisRefresh")
    public String getOperationRecords(Model model, RecordQueryCondition recordQueryCondition) {
        Account user = SecurityUtils.getLoginUser();
        if (!user.isAdmin()) {
            recordQueryCondition.setJobNum(user.getJobNum());
        }
        Page<DossierOperationRecord> dossierOperationRecords = userService.getOperationRecords(recordQueryCondition);
        model.addAttribute("dossierOperationRecords", dossierOperationRecords);
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
        Account user = SecurityUtils.getLoginUser();
        RecordQueryCondition recordQueryCondition = new RecordQueryCondition();
        recordQueryCondition.setPageNum(0);
        recordQueryCondition.setPageSize(10);
        if (!user.isAdmin()) {
            recordQueryCondition.setJobNum(user.getJobNum());
        }
        Page<DossierOperationRecord> dossierOperationRecords = userService.getOperationRecords(recordQueryCondition);
        model.addAttribute("recordQueryCondition", recordQueryCondition);
        model.addAttribute("dossierOperationRecords", dossierOperationRecords);
        model.addAttribute("user", user);
        return "user::his-list-div";
    }

    /**
     * 获取待反馈操作记录表
     * @param model
     * @return
     */
    @RequestMapping("/getFeedbackList")
    public String getFeedbackList(Model model) {
        Account user = SecurityUtils.getLoginUser();
        RecordQueryCondition recordQueryCondition = new RecordQueryCondition(0, 10, 2);
        Page<DossierOperationRecord> dossierOperationRecords = userService.getOperationRecords(recordQueryCondition);
        model.addAttribute("recordQueryCondition", recordQueryCondition);
        model.addAttribute("dossierOperationRecords", dossierOperationRecords);
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
        Page<Account> users = userService.getUsers(accountQueryCondition);
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
        Page<Account> users = userService.getUsers(accountQueryCondition);
        model.addAttribute("accountQueryCondition", accountQueryCondition);
        model.addAttribute("users", users);
        model.addAttribute("user", SecurityUtils.getLoginUser());
        return "user::account-list-div";
    }

    /**
     * 根据管理员提交表单刷新账户操作记录表
     * @param model
     * @param accountOperationQueryCondition
     * @return
     */
    @RequestMapping("/operationRefresh")
    public String getOperations(Model model, AccountOperationQueryCondition accountOperationQueryCondition) {
        Page<UserOperationRecord> userOperationRecords = userService.getUserOperationRecords(accountOperationQueryCondition);
        model.addAttribute("userOperationRecords", userOperationRecords);
        model.addAttribute("user", SecurityUtils.getLoginUser());
        return "user::operation-list-div";
    }

    /**
     * 重置账号操作记录表
     * @param model
     * @return
     */
    @RequestMapping("/operationReset")
    public String resetOperations(Model model) {
        AccountOperationQueryCondition accountOperationQueryCondition = new AccountOperationQueryCondition(0, 10);
        Page<UserOperationRecord> userOperationRecords = userService.getUserOperationRecords(accountOperationQueryCondition);
        model.addAttribute("userOperationRecords", userOperationRecords);
        model.addAttribute("user", SecurityUtils.getLoginUser());
        model.addAttribute("accountOperationQueryCondition", accountOperationQueryCondition);
        return "user::operation-list-div";
    }
}
