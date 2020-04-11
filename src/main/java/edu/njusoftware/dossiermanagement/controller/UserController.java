package edu.njusoftware.dossiermanagement.controller;

import edu.njusoftware.dossiermanagement.domain.DossierOperationRecord;
import edu.njusoftware.dossiermanagement.domain.Account;
import edu.njusoftware.dossiermanagement.domain.PDFLine;
import edu.njusoftware.dossiermanagement.domain.UserOperationRecord;
import edu.njusoftware.dossiermanagement.domain.req.AccountOperationQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.AccountQueryCondition;
import edu.njusoftware.dossiermanagement.domain.req.RecordQueryCondition;
import edu.njusoftware.dossiermanagement.service.IDossierService;
import edu.njusoftware.dossiermanagement.service.IUserService;
import edu.njusoftware.dossiermanagement.util.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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
//        StringDifferenceUtils.getTempString("aaaabdff", "baaadf");
//        DossierContentService.test();
//        Dossier dossier = dossierService.getDossier(62);
//        dossierService.processDossierContent(dossier);
//        List<String> strings = IATSpeechRecognizer.RecognizePcmfileByte(pcmPath);
//        FileEncodeUtils.convertingAudioToPcmFormat(sourcePath, pcmPath, 0F, 15F);
        String pdfPath = "C:/Users/Kratos/Desktop/dossier/test/阿达/FACT授权书/(2014)滨刑初字第0079号 受贿罪139页.pdf";
//        PDFFileEncodeUtils.test(pdfPath);
        List<PDFLine> lines = new ArrayList<>();
        lines.add(new PDFLine("案件移送函", 210, 730, 28));
        lines.add(new PDFLine("天津市滨海新区人民法院：", 95, 640, 16));
        lines.add(new PDFLine("关于天津市滨海新区人民检察院提起公诉的被告人刘瑾", 125, 620, 16));
        lines.add(new PDFLine("钊受贿罪一案，塘沽审判区已于2014年10月23日立案。", 95, 595, 16));
        lines.add(new PDFLine("经审查，本案被告人刘瑾钊身为国家工作人员，利用职务上", 95, 571, 16));
        lines.add(new PDFLine("的便利，非法收受他人财物，为他人谋取利益，其行为已构", 95, 548, 16));
        lines.add(new PDFLine("成受贿罪。根据天津市滨海新区人民法院机关刑事案件受理", 95, 525, 16));
        lines.add(new PDFLine("范围的相关规定，现将该案移送机关刑事审判庭审理，请查", 95, 502, 16));
        lines.add(new PDFLine("收。", 95, 480, 16));
        PDFFileEncodeUtils.alterPageContent(pdfPath, lines, 0);
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
        model.addAttribute("accountOperationQueryCondition", accountOperationQueryCondition);
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
